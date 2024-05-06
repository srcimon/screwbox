package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;

import java.awt.*;
import java.util.function.Supplier;

public class FirewallRenderer implements Renderer {

    private final Renderer next;

    public FirewallRenderer(final Renderer next) {
        this.next = next;
    }

    @Override
    public void updateGraphicsContext(final Supplier<Graphics2D> graphicsSupplier, final Size canvasSize) {
        next.updateGraphicsContext(graphicsSupplier, canvasSize);
    }

    @Override
    public void fillWith(Color color) {
        if (!color.opacity().isZero()) {
            next.fillWith(color);
        }
    }

    @Override
    public void fillWith(Sprite sprite, SpriteFillOptions options) {
        if (!options.opacity().isZero()) {
            next.fillWith(sprite, options);
        }
    }

    @Override
    public void drawText(Offset offset, String text, SystemTextDrawOptions options) {
        if (!options.color().opacity().isZero()) {
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawRectangle(Offset offset, Size size, RectangleDrawOptions options) {
        if (!options.color().opacity().isZero() && size.isValid()) {
            next.drawRectangle(offset, size, options);
        }
    }

    @Override
    public void drawLine(Offset from, Offset to, LineDrawOptions options) {
        if (!options.color().opacity().isZero()) {
            next.drawLine(from, to, options);
        }
    }

    @Override
    public void drawCircle(Offset offset, int radius, CircleDrawOptions options) {
        if (!options.color().opacity().isZero() && radius > 0) {
            next.drawCircle(offset, radius, options);
        }
    }

    @Override
    public void drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options);
        }
    }

    @Override
    public void drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip) {
        if (!options.opacity().isZero()) {
            next.drawSprite(sprite, origin, options, clip);
        }
    }

    @Override
    public void drawText(Offset offset, String text, TextDrawOptions options) {
        if (!options.opacity().isZero() && options.scale() > 0) {
            next.drawText(offset, text, options);
        }
    }

    @Override
    public void drawSpriteBatch(SpriteBatch spriteBatch) {
        next.drawSpriteBatch(spriteBatch);
    }
}
