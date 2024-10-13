package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class DefaultScreen implements Screen, Viewport {

    private final WindowFrame frame;
    private final Robot robot;
    private final Viewport viewport;
    private Sprite lastScreenshot;
    private Rotation rotation = Rotation.none();
    private Rotation shake = Rotation.none();

    public DefaultScreen(final WindowFrame frame, final Robot robot, final Viewport viewport) {
        this.frame = frame;
        this.robot = robot;
        this.viewport = viewport;
    }

    @Override
    public Screen fillWith(final Color color) {
        viewport.fillWith(color);
        return this;
    }

    @Override
    public Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options) {
        viewport.drawRectangle(origin, size, options);
        return this;
    }

    @Override
    public Screen drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        viewport.drawLine(from, to, options);
        return this;
    }

    @Override
    public Screen drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        viewport.drawCircle(offset, radius, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        viewport.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        viewport.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        viewport.drawText(offset, text, options);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final TextDrawOptions options) {
        viewport.drawText(offset, text, options);
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
        lastScreenshot = Sprite.fromImage(screenCapture);
        return lastScreenshot;
    }

    @Override
    public Screen fillWith(final Sprite sprite, final SpriteFillOptions options) {
        viewport.fillWith(sprite, options);
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
    public ScreenBounds bounds() {
        return new ScreenBounds(Offset.origin(), size());
    }

    @Override
    public Screen drawSpriteBatch(SpriteBatch spriteBatch) {
        viewport.drawSpriteBatch(spriteBatch);
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

    private ScreenBounds screenBounds() {
        return new ScreenBounds(Offset.origin(), size());
    }
}
