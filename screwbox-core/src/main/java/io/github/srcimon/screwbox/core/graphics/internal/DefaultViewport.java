package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
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

import java.util.function.Supplier;

//TODO if this only delegates -> delete it
//TODO add finals
public class DefaultViewport implements Viewport {

    private final Renderer renderer;

    public DefaultViewport(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Viewport fillWith(Color color) {
        renderer.fillWith(color);
        return this;
    }

    @Override
    public Viewport drawRectangle(Offset origin, Size size, RectangleDrawOptions options) {
        renderer.drawRectangle(origin, size, options);
        return this;
    }

    @Override
    public Viewport drawLine(Offset from, Offset to, LineDrawOptions options) {
        renderer.drawLine(from, to, options);
        return this;
    }

    @Override
    public Viewport drawCircle(Offset offset, int radius, CircleDrawOptions options) {
        renderer.drawCircle(offset, radius, options);
        return this;
    }

    @Override
    public Viewport drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Viewport drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options);
        return this;
    }

    @Override
    public Viewport drawText(Offset offset, String text, SystemTextDrawOptions options) {
        renderer.drawText(offset, text, options);
        return this;
    }

    @Override
    public Viewport drawText(Offset offset, String text, TextDrawOptions options) {
        renderer.drawText(offset, text, options);
        return this;
    }

    @Override
    public Viewport fillWith(Sprite sprite, SpriteFillOptions options) {
        renderer.fillWith(sprite, options);
        return this;
    }

    @Override
    public Viewport drawSpriteBatch(SpriteBatch spriteBatch) {
        renderer.drawSpriteBatch(spriteBatch);
        return this;
    }
}
