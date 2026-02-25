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
            double maxDistance = 900.0; // Maximale Reichweite der Welle

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
            drawDeepSeaOdyssey(canvasGraphics, screenBuffer);

//            canvasGraphics.drawImage(screenBuffer, 0, 0, null);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
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

        // Luftblasen (Pures Weiß, sehr klein)
        g2d.setColor(new Color(255, 255, 255, 80));
        for(int i=0; i<5; i++) {
            int bx = (int)((time * (i+1) * 0.1) % w);
            int by = h - (int)((time * 0.2 + i*100) % h);
            g2d.drawOval(bx, by, 4, 4);
        }
    }
    static long startTime = System.currentTimeMillis();

    public void drawMedievalOverlay(Graphics g, VolatileImage screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        long seed = 110L;
        double time = System.currentTimeMillis() / 3500.0;

        // 1. NAHTLOSE GLAS-REFRAKTION
        int segmentH = 50;
        for (int y = 0; y < h; y += segmentH) {
            double glassNoise = PerlinNoise.generatePerlinNoise3d(seed, 0, y * 0.008, time);

            // Versatz und Stauchung
            int offsetX = (int) (glassNoise * 15);
            int stretch = (int)(Math.abs(glassNoise) * 5);

            g2d.setComposite(AlphaComposite.SrcOver);

            // TRICK: Wir nehmen ein etwas größeres Quell-Rechteck (+2 Pixel oben/unten/links/rechts)
            // Das füllt die Lücken, die durch die Verzerrung entstehen könnten.
            int pad = 2;
            g2d.drawImage(screenBuffer,
                0, y, w, y + segmentH,                             // Ziel (Destination) bleibt fix
                offsetX - pad, y - stretch - pad,                  // Quelle (Source) mit Padding
                w + offsetX + pad, y + segmentH + stretch + pad,
                null);
        }

        // 2. FARB-ATMOSPHÄRE (Gold & Blau)
        // Wir nutzen weichere Blend-Modi für das mittelalterliche Leuchten
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        // Warmes Gold-Licht von oben
        g2d.setPaint(new GradientPaint(0, 0, new Color(255, 210, 100, 80), w/2, h/2, new Color(0,0,0,0)));
        g2d.fillRect(0, 0, w, h);

        // Kühles Schatten-Blau von unten
        g2d.setPaint(new GradientPaint(w, h, new Color(10, 30, 120, 60), w/2, h/2, new Color(0,0,0,0)));
        g2d.fillRect(0, 0, w, h);

        // 3. LICHT-HALOS (Sanfte Lichtflecken)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for (int i = 0; i < 2; i++) {
            double nX = PerlinNoise.generatePerlinNoise3d(seed + i, time * 0.4, 0, 0);
            double nY = PerlinNoise.generatePerlinNoise3d(seed + i, 0, time * 0.4, 0);

            float lx = (float)(w/2.0 + nX * w/4.0);
            float ly = (float)(h/2.0 + nY * h/4.0);

            RadialGradientPaint halo = new RadialGradientPaint(lx, ly, w * 0.4f,
                new float[]{0f, 1f},
                new Color[]{new Color(255, 255, 220, 40), new Color(0, 0, 0, 0)});

            g2d.setPaint(halo);
            g2d.fillRect(0, 0, w, h);
        }
    }


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

    public void drawNavigableBeeEye(Graphics g, VolatileImage screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();

        if (screenBuffer.contentsLost()) return;

        // 1. Das Originalbild ganz leicht im Hintergrund (als Orientierung)
        g2d.drawImage(screenBuffer, 0, 0, null);

        // Abdunkeln, damit die "Augen" besser hervortreten
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRect(0, 0, w, h);

        int eyeSize = 50;
        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < h; y += eyeSize) {
            int xOffset = (y / eyeSize % 2 == 0) ? 0 : eyeSize / 2;
            for (int x = -xOffset; x < w; x += eyeSize) {

                // Distanz zum Zentrum berechnen (0.0 = Mitte, 1.0 = Rand)
                double dx = (x + eyeSize/2.0) - centerX;
                double dy = (y + eyeSize/2.0) - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;

                // DYNAMISCHER ZOOM:
                // In der Mitte (dist nah 0) ist der Zoom fast 1:1 (Spieler sieht was passiert)
                // Am Rand (dist nah 1) wird das Bild in der Facette winzig
                double localZoom = 0.9 - (dist * 0.6);

                int srcW = (int) (eyeSize / localZoom);
                int srcH = (int) (eyeSize / localZoom);

                // Jedes Auge zeigt den Bereich des Originalbildes, an dem es sich befindet
                // Dadurch bleibt die räumliche Struktur erhalten!
                int srcX = x - (srcW - eyeSize) / 2;
                int srcY = y - (srcH - eyeSize) / 2;

                // Zeichne die Facette
                g2d.drawImage(screenBuffer,
                    x, y, x + eyeSize, y + eyeSize,
                    srcX, srcY, srcX + srcW, srcY + srcH,
                    null);

            }
        }

    }
    public void drawFastFishEye(Graphics g, VolatileImage screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();

        // 1. Validierung des VolatileImage (Verhindert schwarzen Bildschirm)
        if (screenBuffer.contentsLost()) {
            return; // Überspringe Frame, wenn VRAM verloren ging
        }

        // 2. Effekt-Parameter
        int gridSize = 20; // Höher = Schneller, Niedriger = Schöner
        double strength = -0.4; // Wölbung
        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);


        // 3. Gitter-Rendering (Mesh Warp)
        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {

                // Berechne Distanz zum Zentrum für die Kachel
                double dx = x - centerX;
                double dy = y - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;

                // Verzerrungsfaktor (Fischauge)
                double factor = 1.0 + strength * (dist * dist);

                // Ziel-Koordinaten
                int tx = (int) (centerX + dx * factor);
                int ty = (int) (centerY + dy * factor);
                int tw = (int) (gridSize * factor) + 1; // +1 verhindert feine Lücken
                int th = (int) (gridSize * factor) + 1;

                // Hardware-beschleunigtes Zeichnen der Kachel
                g2d.drawImage(screenBuffer,
                    tx, ty, tx + tw, ty + th,     // Ziel-Bereich
                    x, y, x + gridSize, y + gridSize, // Quell-Bereich
                    null);
            }
        }
    }

    public void drawRealityWarpEffect(Graphics g, VolatileImage screenBuffer) {
        Graphics2D g2d = (Graphics2D) g;
        int w = screenBuffer.getWidth();
        int h = screenBuffer.getHeight();
        double time = System.currentTimeMillis() / 1000.0;

        // 1. Hintergrund zeichnen
        g2d.drawImage(screenBuffer, 0, 0, null);

        // 2. Motion Blur & Zoom (Der "Warp"-Effekt)
        // Wir zeichnen das Bild mehrfach skaliert übereinander

        for (int i = 1; i <= 3; i++) {
            double zoom = 1.0 + (Math.sin(time * 2) * 0.02 + (i * 0.05));
            double alpha = 0.15 / i; // Je weiter weg, desto schwächer

            int nw = (int) (w * zoom);
            int nh = (int) (h * zoom);
            int dx = (w - nw) / 2;
            int dy = (h - nh) / 2;

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            g2d.drawImage(screenBuffer, dx, dy, nw, nh, null);
        }

        // 3. Neon-Speed-Lines (Grafisch beeindruckend)
        g2d.setComposite(AlphaComposite.SrcOver);
        Random rand = new Random(42); // Fester Seed für flüssige Bewegung

        // 4. Vignette (Dunkle Ecken für Tiefe)
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)};
        g2d.setPaint(new RadialGradientPaint(w/2, h/2, (float)(w * 0.7), dist, colors));
        g2d.fillRect(0, 0, w, h);
    }

    private void drawAccretionGlow(Graphics2D g2d, int x, int y, int r) {
        float[] dists = {0.0f, 0.5f, 1.0f};
        Color[] colors = {new Color(255, 200, 100, 180), new Color(255, 100, 0, 100), new Color(0, 0, 0, 0)};
        RadialGradientPaint paint = new RadialGradientPaint(x, y, r + 60, dists, colors);
        g2d.setPaint(paint);
        g2d.fillOval(x - (r + 60), y - (r + 60), (r + 60) * 2, (r + 60) * 2);
    }

    public void drawCustomShockwave(Graphics g, VolatileImage img, int x0, int y0,
                                    double radius, double waveWidth, double intensity) {
        int w = img.getWidth();
        int h = img.getHeight();
        int tileSize = 40; // Performance-Hebel: 8-16 ist ideal

        // 1. Das Originalbild einmal komplett als Basis zeichnen
        g.drawImage(img, 0, 0, null);

        // 2. Berechne die Bounding Box der Welle (nur diesen Bereich bearbeiten)
        int limit = (int) (radius + waveWidth + intensity);
        int startX = Math.max(0, (x0 - limit) / tileSize * tileSize);
        int endX   = Math.min(w, (x0 + limit));
        int startY = Math.max(0, (y0 - limit) / tileSize * tileSize);
        int endY   = Math.min(h, (y0 + limit));

        // 3. Nur Kacheln innerhalb der Bounding Box prüfen
        for (int y = startY; y < endY; y += tileSize) {
            for (int x = startX; x < endX; x += tileSize) {

                double dx = x - x0;
                double dy = y - y0;
                double dist = Math.sqrt(dx * dx + dy * dy);

                // Ist die Kachel Teil der Schockwellen-Front?
                double diff = Math.abs(dist - radius);
                if (diff < waveWidth) {
                    // Sinus-Verteilung über die "Länge" der Welle (waveWidth)
                    // Erzeugt einen sanften Übergang (0 -> max -> 0)
                    double falloff = Math.sin((1.0 - diff / waveWidth) * Math.PI);
                    double force = falloff * intensity;

                    // Verschiebung berechnen (Vektor vom Zentrum weg)
                    int ox = (dist == 0) ? 0 : (int) ((dx / dist) * force);
                    int oy = (dist == 0) ? 0 : (int) ((dy / dist) * force);

                    // 4. Nur das verzerrte Subimage zeichnen
                    g.drawImage(img,
                        x + ox, y + oy, x + ox + tileSize, y + oy + tileSize, // Ziel
                        x, y, x + tileSize, y + tileSize,                     // Quelle
                        null);
                }
            }
        }
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
