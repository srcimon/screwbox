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
    private final DefaultPostProcessing postProcessing;
    private Graphics2D lastGraphics;
    private Sprite lastScreenshot;
    private Angle rotation = Angle.none();
    private Angle shake = Angle.none();
    private final DefaultCanvas canvas;
    private VolatileImage screenBuffer;
    private boolean isFlipHorizontally = false;
    private boolean isFlipVertically = false;

    public DefaultScreen(final WindowFrame frame,
                         final Renderer renderer,
                         final Robot robot,
                         final DefaultCanvas canvas,
                         final ViewportManager viewportManager,
                         final GraphicsConfiguration configuration,
                         final DefaultPostProcessing postProcessing) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
        this.canvas = canvas;
        this.viewportManager = viewportManager;
        this.configuration = configuration;
        this.postProcessing = postProcessing;
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
            frame.getCanvas().getBufferStrategy().show();
            final Graphics2D graphics = fetchGraphics();
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

    private Graphics2D fetchGraphics() {
        final var canvasGraphics = fetchCanvasGraphics();
        final Angle angle = absoluteRotation();
        final boolean isInNeedOfScreenBuffer = !angle.isZero() || isFlipHorizontally || isFlipVertically || postProcessing.isActive();
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
            canvasGraphics.setColor(AwtMapper.toAwtColor(configuration.backgroundColor()));
            canvasGraphics.fillRect(0, 0, screenCanvasSize.width(), screenCanvasSize.height());
            canvasGraphics.setTransform(createFlippedAndRotatedTransform(canvasGraphics, screenCanvasSize, angle));
            postProcessing.applyEffects(screenBuffer, canvasGraphics);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
    }

    private AffineTransform createFlippedAndRotatedTransform(final Graphics2D graphics, final Size size, final Angle angle) {
        final var transform = graphics.getTransform();
        if (isFlipHorizontally || isFlipVertically) {
            transform.scale(isFlipHorizontally ? -1 : 1, isFlipVertically ? -1 : 1);
            transform.translate(isFlipHorizontally ? -size.width() : 0, isFlipVertically ? -size.height() : 0);
        }
        if (!angle.isZero()) {
            transform.rotate(angle.radians(), size.width() / 2.0, size.height() / 2.0);
        }
        return transform;
    }

    private Graphics2D fetchCanvasGraphics() {
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
        this.isFlipHorizontally = isFlippedHorizontal;
        return this;
    }

    @Override
    public boolean isFlippedHorizontal() {
        return isFlipHorizontally;
    }

    @Override
    public Screen setFlippedVertical(final boolean isFlippedVertical) {
        this.isFlipVertically = isFlippedVertical;
        return this;
    }

    @Override
    public boolean isFlippedVertical() {
        return isFlipVertically;
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
        final var pointOnWindow = point.substract(position());

        final var flippedX = isFlipHorizontally
            ? pointOnWindow.replaceX(width() - pointOnWindow.x())
            : pointOnWindow;

        final var flippedY = isFlipVertically
            ? flippedX.replaceY(height() - flippedX.y())
            : flippedX;

        return absoluteRotation().invert().rotateAroundCenter(size().center(), flippedY);
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
