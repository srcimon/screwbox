package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Supplier;

import static java.awt.RenderingHints.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultScreen implements Screen, Updatable {

    private final Renderer renderer;
    private final WindowFrame frame;
    private final Robot robot;
    private final ViewportManager viewportManager;
    private Graphics2D lastGraphics;
    private Sprite lastScreenshot;
    private Rotation rotation = Rotation.none();
    private Rotation shake = Rotation.none();
    private final DefaultCanvas canvas;
    private ScreenBounds canvasBounds;

    public DefaultScreen(final WindowFrame frame, final Renderer renderer, final Robot robot, final DefaultCanvas canvas, final ViewportManager viewportManager) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
        this.canvas = canvas;
        this.viewportManager = viewportManager;
    }

    public void updateScreen(final boolean antialiased) {
        final Supplier<Graphics2D> graphicsSupplier = () -> {
            frame.getCanvas().getBufferStrategy().show();
            final Graphics2D graphics = getDrawGraphics();
            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            graphics.setRenderingHint(KEY_DITHERING, VALUE_DITHER_DISABLE);
            graphics.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
            graphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_SPEED);
            graphics.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_SPEED);
            if (antialiased) {
                graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            }
            lastGraphics = graphics;
            return graphics;
        };
        renderer.updateContext(graphicsSupplier);
        renderer.rotate(absoluteRotation(), new ScreenBounds(frame.getCanvasSize()));
        renderer.fillWith(Color.BLACK, new ScreenBounds(frame.getCanvasSize()));
        canvas.updateClip(canvasBounds());
    }

    private Graphics2D getDrawGraphics() {
        try {
            return (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
            // avoid Component must have a valid peer while closing the Window
        } catch (IllegalStateException ignored) {
            return lastGraphics;
        }
    }

    @Override
    public Sprite takeScreenshot() {
        if (!frame.isVisible()) {
            throw new IllegalStateException("window must be opend first to create screenshot");
        }
        final int menuBarHeight = frame.getJMenuBar() == null ? 0 : frame.getJMenuBar().getHeight();
        final Rectangle rectangle = new Rectangle(frame.getX(),
                frame.getY() + frame.getInsets().top + menuBarHeight,
                width(), height());

        final BufferedImage screenCapture = robot.createScreenCapture(rectangle);
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
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y - frame.canvasHeight() + bounds.height);
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
    public Screen setRotation(final Rotation rotation) {
        this.rotation = requireNonNull(rotation, "rotation must not be null");
        return this;
    }

    @Override
    public Rotation rotation() {
        return rotation;
    }

    @Override
    public Rotation shake() {
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
        for (var viewport : viewportManager.activeViewports()) {
            degrees += viewport.camera().swing().degrees();
        }
        this.shake = Rotation.degrees(degrees);
    }

    private ScreenBounds canvasBounds() {
        return isNull(canvasBounds) ? new ScreenBounds(frame.getCanvasSize()) : canvasBounds;
    }

    private void validateCanvasBounds(final ScreenBounds canvasBounds) {
        requireNonNull(canvasBounds, "bounds must not be null");
        if (!new ScreenBounds(frame.getCanvasSize()).intersects(canvasBounds)) {
            throw new IllegalArgumentException("bounds must be on screen");
        }
    }
}
