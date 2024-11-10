package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
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
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.utils.Latch;

import java.awt.*;
import java.util.function.Supplier;

public class StandbyProxyRenderer implements Renderer {

    private final Latch<Renderer> renderer;

    public StandbyProxyRenderer(final Renderer renderer) {
        this.renderer = Latch.of(new StandbyRenderer(), renderer);
    }

    public void toggle() {
        this.renderer.toggle();
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        renderer.active().updateContext(graphics);
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        renderer.active().fillWith(color, clip);
    }

    @Override
    public void rotate(final Rotation rotation, final ScreenBounds clip, final Color backgroundColor) {
        renderer.active().rotate(rotation, clip, backgroundColor);
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        renderer.active().fillWith(sprite, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawText(offset, text, options, clip);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawRectangle(offset, size, options, clip);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawLine(from, to, options, clip);
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawCircle(offset, radius, options, clip);
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawSprite(sprite, origin, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        renderer.active().drawText(offset, text, options, clip);
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch, final ScreenBounds clip) {
        renderer.active().drawSpriteBatch(spriteBatch, clip);
    }
}
