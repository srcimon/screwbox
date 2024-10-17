package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.OffsetRenderer;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Supplier;

import static java.awt.RenderingHints.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultScreen implements Screen {

    private final Renderer renderer;
    private final WindowFrame frame;
    private final Robot robot;
    private Graphics2D lastGraphics;
    private Sprite lastScreenshot;
    private Rotation rotation = Rotation.none();
    private Rotation shake = Rotation.none();
    private final DefaultCanvas canvas;
    private ScreenBounds canvasBounds;

    public DefaultScreen(final WindowFrame frame, final Renderer renderer, final Robot robot, final DefaultCanvas canvas) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
        this.canvas = canvas;
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
        renderer.rotate(absoluteRotation(), getCanvasBounds());
        renderer.fillWith(Color.BLACK, new ScreenBounds(frame.getCanvasSize()));
        canvas.updateClip(getCanvasBounds());
    }

    //TODO export
    private ScreenBounds getCanvasBounds() {
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
    public Screen fillWith(final Color color) {
        canvas.fillWith(color);
        return this;
    }

    @Override
    public Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options) {
        canvas.drawRectangle(origin, size, options);
        return this;
    }

    @Override
    public Screen drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        canvas.drawLine(from, to, options);
        return this;
    }

    @Override
    public Screen drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        canvas.drawCircle(offset, radius, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        canvas.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        canvas.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        canvas.drawText(offset, text, options);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final TextDrawOptions options) {
        canvas.drawText(offset, text, options);
        return this;
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
    public Screen fillWith(final Sprite sprite, final SpriteFillOptions options) {
        canvas.fillWith(sprite, options);
        return this;
    }

    @Override
    public Offset offset() {
        return Offset.origin();
    }

    @Override
    public Offset center() {
        return size().center();
    }

    @Override
    public Size size() {
        return frame.getCanvasSize();
    }

    @Override
    public ScreenBounds bounds() {
        return new ScreenBounds(offset(), size());
    }

    @Override
    public Screen drawSpriteBatch(SpriteBatch spriteBatch) {
        canvas.drawSpriteBatch(spriteBatch);
        return this;
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
        //TODO implement
        //TODO verify on screen and has minimal size
        //TODO after resolution change ->ensure canvas size is still up to date ( alternative: reset canvas size on resolution change?)
        canvasBounds = bounds;
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

    public void setShake(final Rotation shake) {
        this.shake = requireNonNull(shake, "shake must not be null");
    }

    public Canvas createCanvas(final Offset offset, final Size size) {
        //TODO: validate on screen;
        final var offsetRenderer = new OffsetRenderer(offset, renderer);
        return new DefaultCanvas(offsetRenderer, new ScreenBounds(offset, size));
    }
}
