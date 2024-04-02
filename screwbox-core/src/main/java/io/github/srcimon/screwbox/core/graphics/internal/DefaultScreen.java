package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions.scaled;
import static java.awt.RenderingHints.*;
import static java.util.Objects.nonNull;

public class DefaultScreen implements Screen {

    private Renderer renderer;
    private final WindowFrame frame;
    private final Robot robot;
    private Graphics2D lastGraphics;

    public DefaultScreen(final WindowFrame frame, final Renderer renderer, final Robot robot) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
    }

    public void updateScreen(final boolean antialiased) {
        final Supplier<Graphics2D> graphicsSupplier = () -> {
            frame.getCanvas().getBufferStrategy().show();
            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            final Graphics2D graphics = (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();
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
        renderer.updateGraphicsContext(graphicsSupplier, frame.getCanvasSize());
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen fillWith(final Color color) {
        renderer.fillWith(color);
        return this;
    }

    @Override
    public Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options) {
        if (size.width() > 0 && size.height() > 0) {
            renderer.drawRectangle(origin, size, options);
        }
        return this;
    }

    @Override
    public Screen drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from, to, options);
        return this;
    }

    @Override
    public Screen drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        if (radius > 0) {
            renderer.drawCircle(offset, radius, options);
        }
        return this;
    }

    @Override
    public Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderer.drawSprite(sprite, origin, options, clip);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.drawText(offset, text, options);
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
                frame.getCanvas().getWidth(),
                frame.canvasHeight());

        final BufferedImage screenCapture = robot.createScreenCapture(rectangle);
        return Sprite.fromImage(screenCapture);
    }

    @Override
    public Screen drawTextCentered(final Offset offset, final String text, final Pixelfont font,
                                   final Percent opacity, final double scale) {
        final int totalWidth = (int) (font.widthOf(text) * scale);
        drawTextSprites(offset.addX(totalWidth / -2), opacity, scale, font.spritesFor(text), font);
        return this;
    }

    @Override
    public Screen fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.fillWith(sprite, options);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final Pixelfont font, final Percent opacity,
                           final double scale) {
        final List<Sprite> allSprites = font.spritesFor(text);
        drawTextSprites(offset, opacity, scale, allSprites, font);
        return this;
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
    public boolean isVisible(final ScreenBounds bounds) {
        return screenBounds().intersects(bounds);
    }

    @Override
    public boolean isVisible(final Offset offset) {
        return screenBounds().contains(offset);
    }

    @Override
    public Offset position() {
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y - frame.canvasHeight() + bounds.height);
    }

    private void drawTextSprites(final Offset offset, final Percent opacity, final double scale,
                                 final List<Sprite> allSprites,
                                 final Pixelfont font) {
        Offset currentOffset = offset;
        for (final var sprite : allSprites) {
            drawSprite(sprite, currentOffset, scaled(scale).opacity(opacity));
            currentOffset = currentOffset.addX((int) ((sprite.size().width() + font.padding()) * scale));
        }
    }

    private ScreenBounds screenBounds() {
        return new ScreenBounds(Offset.origin(), size());
    }

}
