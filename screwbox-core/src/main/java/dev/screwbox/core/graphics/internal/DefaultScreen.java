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
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Optional;
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
            drawRadialWobble(canvasGraphics, screenBuffer);
          //  canvasGraphics.drawImage(screenBuffer, 0, 0, null);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
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
