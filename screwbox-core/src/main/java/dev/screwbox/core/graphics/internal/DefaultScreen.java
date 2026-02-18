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
    private ScreenBounds canvasBounds;

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

    private VolatileImage screenBuffer;

    private void initializeScreenBuffer() {
        screenBuffer = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .createCompatibleVolatileImage(canvas.width(), canvas.height());
    }

    public void updateScreen(final boolean antialiased) {
        Angle angle = absoluteRotation();
        boolean isInNeedOfScreenBuffer = !angle.isZero();
        final Supplier<Graphics2D> graphicsSupplier = () -> {
            if (nonNull(screenBuffer)) {

                Graphics2D canvasGraphics = getDrawGraphics();
                final var color = configuration.backgroundColor();
                canvasGraphics.setColor(AwtMapper.toAwtColor(color));
                canvasGraphics.fillRect(0, 0, canvas.width(), canvas.height());
                canvasGraphics.rotate(angle.radians(), canvas.width() / 2.0, canvas.height() / 2.0);
//                final ScreenBounds clip = new ScreenBounds(frame.getCanvasSize());
//                renderer.rotate(absoluteRotation(), clip, color);
                canvasGraphics.drawImage(screenBuffer, 0, 0, null);
                //TODO do not delete just clear
            }

            frame.getCanvas().getBufferStrategy().show();
            final Graphics2D graphics;
            if (isInNeedOfScreenBuffer) {
                if (isNull(screenBuffer)) {
                    initializeScreenBuffer();
                } else {
                    //TODO black?
                    screenBuffer.getGraphics().clearRect(0, 0, canvas.width(), canvas.height());
                }
                graphics = screenBuffer.createGraphics();

            } else {
                graphics = getDrawGraphics();
                screenBuffer=null;
            }

            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            ImageOperations.applyHighPerformanceRenderingHints(graphics);
            if (antialiased) {
                graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            }
            lastGraphics = graphics;
            return graphics;
        };
        renderer.updateContext(graphicsSupplier);
        final var color = configuration.backgroundColor();
        final ScreenBounds clip = new ScreenBounds(frame.getCanvasSize());
//        renderer.rotate(absoluteRotation(), clip, color);
        renderer.fillWith(color, clip);
        canvas.updateClip(canvasBounds());
    }

    private Graphics2D getDrawGraphics() {
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
    public Screen setCanvasBounds(final ScreenBounds bounds) {
        validateCanvasBounds(bounds);
        canvasBounds = bounds;
        return this;
    }

    @Override
    public Screen resetCanvasBounds() {
        canvasBounds = null;
        return this;
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

    public Canvas createCanvas(final Offset offset, final Size size) {
        final ScreenBounds bounds = new ScreenBounds(offset, size);
        validateCanvasBounds(bounds);
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

    private ScreenBounds canvasBounds() {
        return isNull(canvasBounds) ? new ScreenBounds(frame.getCanvasSize()) : canvasBounds;
    }

    private void validateCanvasBounds(final ScreenBounds canvasBounds) {
        requireNonNull(canvasBounds, "bounds must not be null");
        final var screenBounds = new ScreenBounds(frame.getCanvasSize());
        Validate.isTrue(() -> screenBounds.intersects(canvasBounds), "bounds must be on screen");
    }
}
