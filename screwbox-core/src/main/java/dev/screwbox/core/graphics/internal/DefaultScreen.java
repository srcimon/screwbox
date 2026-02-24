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
        final boolean isInNeedOfScreenBuffer = !angle.isZero() || isFlipHorizontal || isFlipVertical;
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
            if(!angle.isZero()) {
                transform.rotate(angle.radians(), screenCanvasSize.width() / 2.0, screenCanvasSize.height() / 2.0);
            }
            //TODO shear
            transform.shear(0.0,0.0);
            canvasGraphics.setTransform(transform);
            canvasGraphics.drawImage(screenBuffer, 0, 0, null);
            canvasGraphics.dispose();
        }

        return screenBuffer.createGraphics();
    }

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
