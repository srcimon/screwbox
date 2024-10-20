package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.OffsetRenderer;
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
    private final Camera camera;
    private Graphics2D lastGraphics;
    private Sprite lastScreenshot;
    private Rotation rotation = Rotation.none();
    private Rotation shake = Rotation.none();
    private final DefaultCanvas canvas;
    private ScreenBounds canvasBounds;

    public DefaultScreen(final WindowFrame frame, final Renderer renderer, final Robot robot, final DefaultCanvas canvas, final Camera camera) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
        this.canvas = canvas;
        this.camera = camera;
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
        renderer.rotate(absoluteRotation(), canvasBounds());//TODO rotate around itself would be better > fix sprite fade animation then
        renderer.fillWith(Color.BLACK, new ScreenBounds(frame.getCanvasSize()));
        canvas.updateClip(canvasBounds());
    }

    @Override
    public ScreenBounds canvasBounds() {
        return isNull(canvasBounds) ? new ScreenBounds(frame.getCanvasSize()) : canvasBounds;
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
        final var offsetRenderer = new OffsetRenderer(offset, renderer);
        return new DefaultCanvas(offsetRenderer, bounds);
    }

    void validateCanvasBounds(final ScreenBounds canvasBounds) {
        requireNonNull(canvasBounds, "bounds must not be null");
        if (!new ScreenBounds(frame.getCanvasSize()).intersects(canvasBounds)) {
            throw new IllegalArgumentException("bounds must be on screen");
        }
    }

    @Override
    public void update() {
        this.shake = camera.swing();
    }
}
