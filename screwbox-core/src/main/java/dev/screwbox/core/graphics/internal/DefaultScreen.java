package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.utils.Validate;
import dev.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
            drawFastFishEye(canvasGraphics, screenBuffer);

//            canvasGraphics.drawImage(screenBuffer, 0, 0, null);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
    }

    static long startTime = System.currentTimeMillis();

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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

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
