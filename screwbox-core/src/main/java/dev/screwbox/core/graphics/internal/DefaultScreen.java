package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.utils.PerlinNoise;
import dev.screwbox.core.utils.Validate;
import dev.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultScreen implements Screen, Updatable {

    private final Renderer renderer;
    private final WindowFrame frame;
    private final Robot robot;
    private final ViewportManager viewportManager;
    private final GraphicsConfiguration configuration;
    private Graphics2D lastGraphics;
    private Sprite lastScreenshot;
    private Angle rotation = Angle.none();
    private Angle shake = Angle.none();
    private final DefaultCanvas canvas;
    private VolatileImage screenBuffer;
    private boolean isFlipHorizontal = false;
    private boolean isFlipVertical = false;

    public DefaultScreen(final WindowFrame frame,
                         final Renderer renderer,
                         final Robot robot,
                         final DefaultCanvas canvas,
                         final ViewportManager viewportManager,
                         final GraphicsConfiguration configuration) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
        this.canvas = canvas;
        this.viewportManager = viewportManager;
        this.configuration = configuration;
    }

    public void updateScreen() {
        renderer.updateContext(createGraphicsSupplier());
        final var color = configuration.backgroundColor();
        final ScreenBounds clip = new ScreenBounds(frame.getCanvasSize());
        renderer.fillWith(color, clip);
        canvas.updateClip(new ScreenBounds(frame.getCanvasSize()));
    }

    private Supplier<Graphics2D> createGraphicsSupplier() {
        return () -> {
            final Graphics2D canvasGraphics = getCanvasGraphics();
            final Graphics2D graphics = fetchGraphics(canvasGraphics);
            frame.getCanvas().getBufferStrategy().show();
            ImageOperations.applyHighPerformanceRenderingHints(graphics);
            if (configuration.isUseAntialiasing()) {
                graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            }
            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            lastGraphics = graphics;
            return graphics;
        };
    }

    private Graphics2D fetchGraphics(final Graphics2D canvasGraphics) {
        final Angle angle = absoluteRotation();
        final boolean isInNeedOfScreenBuffer = true;//!angle.isZero() || isFlipHorizontal || isFlipVertical;
        if (!isInNeedOfScreenBuffer) {
            screenBuffer = null;
            return canvasGraphics;
        }
        final var screenCanvasSize = frame.getCanvasSize();
        if (isNull(screenBuffer)
            || screenCanvasSize.width() != screenBuffer.getWidth()
            || screenCanvasSize.height() != screenBuffer.getHeight()) {
            screenBuffer = ImageOperations.createVolatileImage(screenCanvasSize);
        } else {
            final var transform = canvasGraphics.getTransform();
            canvasGraphics.setColor(AwtMapper.toAwtColor(configuration.backgroundColor()));
            canvasGraphics.fillRect(0, 0, screenCanvasSize.width(), screenCanvasSize.height());
            // TODO concat to one command or implement pattern for additional actions / one way or the other
            if (isFlipHorizontal) {
                transform.scale(-1, 1);
                transform.translate(-screenCanvasSize.width(), 0);
            }
            if (isFlipVertical) {
                transform.scale(1, -1);
                transform.translate(0, -screenCanvasSize.height());
            }
            if (!angle.isZero()) {
                transform.rotate(angle.radians(), screenCanvasSize.width() / 2.0, screenCanvasSize.height() / 2.0);
            }
            //TODO shear
            canvasGraphics.setTransform(transform);
// Parameter für 1270x800
            int x0 = 635;
            int y0 = 400;
            double maxDistance = 50.0; // Maximale Reichweite der Welle

// 1. Fortschritt berechnen (z.B. über 2 Sekunden)
            double elapsed = (System.currentTimeMillis() - startTime) / 2000.0;
            if (elapsed > 1.0) {
                startTime = System.currentTimeMillis();
            }

// 2. Radius und weiches Fade-out berechnen
            double currentRadius = elapsed * maxDistance;

// "Smooth-Out" Faktor: Die Welle verliert an Energie.
// Wir nutzen Math.pow für ein organischeres Abklingen (quadratisch)
            double fadeOut = Math.pow(1.0 - elapsed, 2.0);

            double currentIntensity = 45.0 * fadeOut; // Startet bei 45px, endet bei 0px
            double currentWaveWidth = 120.0 * (1.0 + elapsed); // Welle wird breiter, während sie wandert

// 3. Aufruf
         if(shockwaves.isEmpty()) {
             for (int i = 0; i < 3; i++) {
                 shockwaves.add(new Shockwave(1200, 600));
             }
         }
            for (Shockwave s : shockwaves) {
                s.update(1200, 600);
            }
            if (isNull(screenBuffer2)
                || screenCanvasSize.width() != screenBuffer2.getWidth()
                || screenCanvasSize.height() != screenBuffer2.getHeight()) {
                screenBuffer2 = ImageOperations.createVolatileImage(screenCanvasSize);
            }

            Graphics2D graphics2 = screenBuffer2.createGraphics();
            drawMultipleShockwaves(graphics2, screenBuffer, shockwaves);
            graphics2.dispose();
            drawDeepSeaOdyssey(canvasGraphics, screenBuffer2);
//            canvasGraphics.drawImage(screenBuffer, 0, 0, null);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
    }
    VolatileImage screenBuffer2;

    java.util.List<Shockwave> shockwaves = new ArrayList<>();
    public class Shockwave {
        public double x, y, radius, waveWidth, intensity, maxRadius;
        private double initialIntensity;

        public Shockwave(int screenW, int screenH) {
            init(screenW, screenH);
        }

        public void init(int screenW, int screenH) {
            Random rnd = new Random();
            this.x = rnd.nextInt(screenW);
            this.y = rnd.nextInt(screenH);
            this.radius = 0;

            // 5. Massiv erhöhter Max-Radius für lange Lebensdauer
            // Die Welle kann nun fast den halben Bildschirm füllen
            this.maxRadius = 140 + rnd.nextInt(400);

            this.initialIntensity = 15 + rnd.nextDouble() * 10;
            this.intensity = initialIntensity;
        }

        public void update(int screenW, int screenH) {
            this.radius += 0.5;

            // 2. Dynamische Wellenbreite: Die Welle wird breiter, während sie wandert
            // Startet bei 30 und wächst auf bis zu 100 Pixel Dicke
            this.waveWidth = 30 + (radius * 0.2);

            // 3. Fallout-Kurve: Nicht linear, sondern "smooth"
            // Bleibt länger stark und fadet erst am Ende schnell aus
            double progress = radius / maxRadius;
            double smoothFade = Math.cos(progress * Math.PI / 2); // Cosinus-Fade
            this.intensity = initialIntensity * smoothFade;

            // 4. Reset erst bei deutlich größerem Radius
            if (this.radius >= maxRadius || this.intensity < 0.1) {
                init(screenW, screenH);
            }
        }
    }

    public void drawMultipleShockwaves(Graphics g, VolatileImage img, java.util.List<Shockwave> waves) {
        int w = img.getWidth();
        int h = img.getHeight();
        int tileSize = 20; // Etwas kleiner für weichere Übergänge bei Überlappung

        // 1. Zuerst das Basisbild einmal zeichnen
        g.drawImage(img, 0, 0, null);

        // 2. Den Bildschirm in Kacheln durchlaufen
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                // 3. Für jede Kachel den Einfluss ALLER Wellen berechnen
                for (Shockwave s : waves) {
                    double dx = x - s.x;
                    double dy = y - s.y;
                    double distSq = dx * dx + dy * dy; // Quadrat für Performance
                    double dist = Math.sqrt(distSq);

                    double diff = Math.abs(dist - s.radius);
                    if (diff < s.waveWidth) {
                        double falloff = Math.sin((1.0 - diff / s.waveWidth) * Math.PI);
                        double force = falloff * s.intensity;

                        if (dist > 0) {
                            totalOx += (dx / dist) * force;
                            totalOy += (dy / dist) * force;
                            active = true;
                        }
                    }
                }

                // 4. Nur zeichnen, wenn mindestens eine Welle die Kachel beeinflusst
                if (active) {
                    int destX = x + (int)totalOx;
                    int destY = y + (int)totalOy;

                    g.drawImage(img,
                        destX, destY, destX + tileSize, destY + tileSize, // Ziel (verschoben)
                        x, y, x + tileSize, y + tileSize,                 // Quelle (original)
                        null);
                }
            }
        }
    }

    public void drawDeepSeaOdyssey(Graphics g, VolatileImage screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        long time = System.currentTimeMillis();
        double zTime = time / 2000.0;


        // 2. UNDERWATER WOBBLE (Perlin-Distortion)
        // Wir nutzen das Gitter-System vom Fischauge, aber biegen es organisch
        int gridSize = 32;
        double centerX = w / 2.0, centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {
                // Perlin Noise für die Strömung
                double noise = PerlinNoise.generatePerlinNoise3d(110L, x * 0.005, y * 0.005, zTime);

                // Fischauge-Faktor
                double dx = x - centerX, dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;
                double fischEye = 1.0 - 0.2 * (dist * dist);

                int tx = (int) (centerX + dx * fischEye + noise * 20);
                int ty = (int) (centerY + dy * fischEye + Math.sin(zTime + x*0.01) * 10);

                g2d.setComposite(AlphaComposite.SrcOver);
                g2d.drawImage(screenBuffer, tx, ty, tx + gridSize + 1, ty + gridSize + 1,
                    x, y, x + gridSize, y + gridSize, null);
            }
        }


        // 4. DEEP SEA LIGHTING (Caustics & Fog)
        float[] d = {0f, 0.6f, 1f};
        Color[] c = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};
        RadialGradientPaint deepBlue = new RadialGradientPaint(w/2f, h/2f, (float)(w*0.8), d, c);
        g2d.setPaint(deepBlue);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.fillRect(0, 0, w, h);

        if (bubbleX == null) {
            int bCount = 15; // Mehr Blasen für bessere Atmo
            bubbleX = new float[bCount]; bubbleY = new float[bCount];
            bubbleSpeed = new float[bCount]; bubbleSize = new float[bCount];
            Random r = new Random();
            for(int i=0; i<bCount; i++) {
                bubbleX[i] = r.nextInt(w);
                bubbleY[i] = r.nextInt(h);
                bubbleSpeed[i] = 1.0f + r.nextFloat() * 2.0f; // Individuelle Geschwindigkeit
                bubbleSize[i] = 2 + r.nextInt(5);             // Unterschiedliche Größen
            }
        }

// 5. REALISTISCHE LUFTBLASEN
        for (int i = 0; i < bubbleX.length; i++) {
            // Perlin Noise für das horizontale "Tandeln" (Wobble)
            // Wir nutzen x, y und die Zeit, damit jede Blase eigenständig auf die Strömung reagiert
            double horizontalDrift = PerlinNoise.generatePerlinNoise3d(220L, bubbleX[i] * 0.01, bubbleY[i] * 0.01, zTime);

            // Bewegung: Auftrieb + Sinus-Wobble + Perlin-Drift
            bubbleY[i] -= bubbleSpeed[i];
            bubbleX[i] += (float)(horizontalDrift * 2.0 + Math.sin(zTime + i) * 0.5);

            // Screen Wrap (unten neu spawnen, wenn oben aus dem Bild)
            if (bubbleY[i] < -20) {
                bubbleY[i] = h + 20;
                bubbleX[i] = new Random().nextInt(w);
            }
            // Horizontales Wrapping
            bubbleX[i] = (bubbleX[i] + w) % w;

            // Zeichnen mit Tiefenwirkung (kleinere Blasen sind blasser)
            int alpha = (int)(50 + (bubbleSize[i] * 20));
            g2d.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

            // Die Blase leicht deformieren (etwas oval beim Aufstieg)
            int bw = (int)bubbleSize[i];
            int bh = (int)(bubbleSize[i] * 1.1); // Leicht in die Länge gezogen

            g2d.fillOval((int)bubbleX[i], (int)bubbleY[i], bw, bh);

            // Optional: Ein kleiner Glanzpunkt
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval((int)bubbleX[i] + 1, (int)bubbleY[i] + 1, bw/3, bh/3);
        }
    }
    static long startTime = System.currentTimeMillis();
    private float[] bubbleX, bubbleY, bubbleSpeed, bubbleSize;

    public void drawLocalHeatHaze(Graphics g, VolatileImage screenBuffer, java.util.List<Rectangle> heatZones) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        double time = System.currentTimeMillis() / 100.0;

        // 1. Zuerst das komplette Originalbild zeichnen
        g2d.drawImage(screenBuffer, 0, 0, null);

        // 2. Nur die Heat-Zonen bearbeiten
        for (Rectangle zone : heatZones) {
            // Wir clippen die Graphics, damit wir nicht aus dem Rechteck zeichnen
            Shape oldClip = g2d.getClip();
            g2d.setClip(zone);

            int segmentH = 2;
            for (int y = zone.y; y < zone.y + zone.height; y += segmentH) {

                // Intensität basierend auf der vertikalen Position innerhalb des Rechtecks
                // (Unten im Rechteck stärker als oben)
                double localFactor = (double)(y - zone.y) / zone.height;

                double wave = (Math.sin(time * 1.5 + y * 0.1) * 4 +
                               Math.sin(time * 3.7 + y * 0.5) * 2) * localFactor;

                int offsetX = (int) wave;
                int offsetY = (int) (Math.abs(Math.sin(time * 0.5 + y * 0.05)) * 3 * localFactor);

                g2d.drawImage(screenBuffer,
                    zone.x, y, zone.x + zone.width, y + segmentH,
                    zone.x + offsetX, y + offsetY, zone.x + zone.width + offsetX, y + segmentH + offsetY,
                    null);
            }

            // 3. Gradient direkt über die Zone legen
            GradientPaint heatGlow = new GradientPaint(
                zone.x, zone.y, new Color(255, 150, 50, 0),
                zone.x, zone.y + zone.height, new Color(255, 100, 0, 30)
            );
            g2d.setPaint(heatGlow);
            g2d.fill(zone);

            // Clip zurücksetzen
            g2d.setClip(oldClip);
        }
    }



    private void drawAccretionGlow(Graphics2D g2d, int x, int y, int r) {
        float[] dists = {0.0f, 0.5f, 1.0f};
        Color[] colors = {new Color(255, 200, 100, 180), new Color(255, 100, 0, 100), new Color(0, 0, 0, 0)};
        RadialGradientPaint paint = new RadialGradientPaint(x, y, r + 60, dists, colors);
        g2d.setPaint(paint);
        g2d.fillOval(x - (r + 60), y - (r + 60), (r + 60) * 2, (r + 60) * 2);
    }




    public void drawSplitEffect(Graphics g, VolatileImage screenBuffer) {
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        double t = System.currentTimeMillis() / 400.0;

        // Maximale Verschiebung in Pixeln
        int offset = (int) (Math.sin(t) * 15);

        for (int y = 0; y < h; y++) {
            // Jede zweite Zeile geht in die andere Richtung
            int currentOffset = (y % 2 == 0) ? offset : -offset;

            g.drawImage(screenBuffer,
                currentOffset, y, w + currentOffset, y + 1,
                0, y, w, y + 1,
                null);
        }
    }

    public void drawImpressiveSplitEffect(Graphics g, VolatileImage screenBuffer) {
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        double time = System.currentTimeMillis() / 500.0;

        // Parameter für den Look
        double frequency = 0.05; // Wie viele Wellen auf dem Schirm sind
        double amplitude = 25.0;  // Wie stark die Verzerrung ist
        int rgbSplit = 4;        // Versatz der Farbkanäle (Glitch-Effekt)

        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < h; y += 2) { // Schrittweite 2 für bessere Performance
            // Berechne horizontalen Versatz basierend auf Sinus + Zeit
            int offset = (int) (Math.sin(y * frequency + time) * amplitude);

            // Zeichne den "Glitched" Background (Rot-Kanal leicht versetzt)
            g2d.setComposite(AlphaComposite.SrcOver);

            // Haupt-Bild mit leichtem Versatz für den RGB-Effekt
            // Wir zeichnen hier 2-Pixel-Streifen für einen Scanline-Look
            g.drawImage(screenBuffer,
                offset, y, w + offset, y + 2,
                0, y, w, y + 2,
                null);

            // Optional: Ein zweiter Pass mit Transparenz für chromatische Aberration
            if (Math.abs(offset) > amplitude * 0.8) { // Nur bei starken Ausschlägen blitzen
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g.drawImage(screenBuffer,
                    offset + rgbSplit, y, w + offset + rgbSplit, y + 2,
                    0, y, w, y + 2,
                    null);
            }
        }

        // Optional: Ein feines Scanline-Overlay für den Retro-Touch
        g.setColor(new Color(0, 0, 0, 40));
        for (int y = 0; y < h; y += 4) {
            g.drawLine(0, y, w, y);
        }
    }

    public void drawWaveEffect(Graphics g, VolatileImage screenBuffer) {
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();

        double time = System.currentTimeMillis() / 500.0; // Geschwindigkeit
        double waveIntensity = 20.0; // Wie weit schlägt der Wobble aus (Pixel)
        double frequency = 0.05;     // Wie eng liegen die Wellen beieinander

        int rowHeight= 4;
        for (int y = 0; y < h; y+=rowHeight) {
            // Berechne den Versatz für diese spezifische Zeile
            int offsetX = (int) (Math.sin((y * frequency) + time) * waveIntensity);

            // Zeichne einen 1-Pixel hohen Streifen des Bildes
            // drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
            g.drawImage(screenBuffer,
                offsetX, y, w + offsetX, y + rowHeight, // Ziel-Position (auf dem Canvas)
                0, y, w, y + rowHeight,                 // Quell-Bereich (vom Buffer)
                null);
        }
    }

    public void drawRadialWobble(Graphics g, VolatileImage screenBuffer) {
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        double time = System.currentTimeMillis() / 500.0;
        int iterations = 30; // Anzahl der Segmente von außen nach innen

        // Wir arbeiten uns vom Rand (groß) zur Mitte (klein) vor
        for (int i = 0; i < iterations; i++) {
            // Der Sinus-Shift ist abhängig von der Distanz zum Zentrum
            double wave = Math.sin(time + (i * 0.3));
            int offset = (int) (wave * 12); // Stärke des Auschlags

            // Berechne die Größe des aktuellen "Rahmens"
            int stepW = centerX / iterations;
            int stepH = centerY / iterations;

            // Quell-Koordinaten (Original-Rechteck dieses Schritts)
            int sx1 = i * stepW;
            int sy1 = i * stepH;
            int sx2 = w - sx1;
            int sy2 = h - sy1;

            // Ziel-Koordinaten (mit dem Sinus-Offset versetzt)
            // Das Bild wird hier leicht "aufgepumpt" oder "eingesaugt"
            int dx1 = sx1 - offset;
            int dy1 = sy1 - offset;
            int dx2 = sx2 + offset;
            int dy2 = sy2 + offset;

            // Zeichne nur diesen spezifischen Rahmen
            g.drawImage(screenBuffer,
                dx1, dy1, dx2, dy2,
                sx1, sy1, sx2, sy2,
                null);
        }
    }
//TODO shock wave

    private Graphics2D getCanvasGraphics() {
        try {
            return (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
            // avoid Component must have a valid peer while closing the Window
        } catch (final IllegalStateException ignored) {
            return lastGraphics;
        }
    }

    @Override
    public Sprite takeScreenshot() {
        if (!frame.isVisible()) {
            throw new IllegalStateException("window must be opened first to create screenshot");
        }
        final var canvasOffset = frame.getCanvasOffset();
        final var rectangle = new Rectangle(canvasOffset.x(), canvasOffset.y(), width(), height());
        final var screenCapture = robot.createScreenCapture(rectangle);
        lastScreenshot = Sprite.fromImage(screenCapture);
        return lastScreenshot;
    }

    @Override
    public Size size() {
        return frame.getCanvasSize();
    }

    @Override
    public Optional<Sprite> lastScreenshot() {
        return Optional.ofNullable(lastScreenshot);
    }

    @Override
    public Offset position() {
        return frame.getCanvasOffset();
    }

    @Override
    public Screen setFlippedHorizontal(final boolean isFlippedHorizontal) {
        this.isFlipHorizontal = isFlippedHorizontal;
        return this;
    }

    @Override
    public boolean isFlippedHorizontal() {
        return isFlipHorizontal;
    }

    @Override
    public Screen setFlippedVertical(final boolean isFlippedVertical) {
        this.isFlipVertical = isFlippedVertical;
        return this;
    }

    @Override
    public boolean isFlippedVertical() {
        return isFlipVertical;
    }

    @Override
    public Screen setRotation(final Angle rotation) {
        this.rotation = requireNonNull(rotation, "rotation must not be null");
        return this;
    }

    @Override
    public Angle rotation() {
        return rotation;
    }

    @Override
    public Angle shake() {
        return shake;
    }

    @Override
    public Offset translateMonitorToScreen(final Offset point) {
        var offset = point.substract(position());
        if (isFlipHorizontal) {
            offset = Offset.at(width() - offset.x(), offset.y());
        }
        if (isFlipVertical) {
            offset = Offset.at(offset.x(), height() - offset.y());
        }
        if (!absoluteRotation().isZero()) {
            offset = absoluteRotation().invert().rotateAroundCenter(size().center(), offset);
        }
        return offset;
    }

    public Canvas createCanvas(final Offset offset, final Size size) {
        final ScreenBounds bounds = new ScreenBounds(offset, size);
        requireNonNull(bounds, "bounds must not be null");
        final var screenBounds = new ScreenBounds(frame.getCanvasSize());
        Validate.isTrue(() -> screenBounds.intersects(bounds), "bounds must be on screen");
        return new DefaultCanvas(renderer, bounds);
    }

    @Override
    public void update() {
        double degrees = viewportManager.defaultViewport().camera().swing().degrees();
        for (var viewport : viewportManager.viewports()) {
            degrees += viewport.camera().swing().degrees();
        }
        this.shake = Angle.degrees(degrees);
    }
}
