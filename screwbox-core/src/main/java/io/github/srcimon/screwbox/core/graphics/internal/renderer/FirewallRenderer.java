package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Prevent uneccesary and invalid rendering tasks hitting the actual render.
 */
public class FirewallRenderer implements Renderer {

    private final Renderer next;
    private ScreenBounds screen;

    public FirewallRenderer(final Renderer next) {
        this.next = next;
    }

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        next.updateGraphicsContext(graphicsSupplier, canvasSize);
        screen = new ScreenBounds(Offset.origin(), canvasSize);
    }

    @Override
    public void fillWith(final Color color) {
        if (!color.opacity().isZero()) {
            next.fillWith(color);
        }
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        if (!options.opacity().isZero()) {
            next.fillWith(sprite, options);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        if (!options.color().opacity().isZero() && !text.isEmpty()) { //TODO(LEFT = options.alignment() || offset.x() > screen.size().width()))
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        final var rectangleBounds = new ScreenBounds(offset, size);
        if (!options.color().opacity().isZero() && size.isValid() && screen.intersects(rectangleBounds)) {
            next.drawRectangle(offset, size, options);
        }
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        //TODO skipp when not in drawing area
        if (!options.color().opacity().isZero() && options.strokeWidth() > 0) {
            next.drawLine(from, to, options);
        }
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        final var circleBounds = new ScreenBounds(offset.x() - radius, offset.y() - radius, radius * 2, radius * 2);
        if (!options.color().opacity().isZero() && radius > 0 && circleBounds.intersects(screen)) {
            next.drawCircle(offset, radius, options);
        }
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        if (!options.opacity().isZero() && options.scale() > 0) {
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        next.drawSpriteBatch(spriteBatch);
    }
}
