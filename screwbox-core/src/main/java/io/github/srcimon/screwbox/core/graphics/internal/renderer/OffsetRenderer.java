package io.github.srcimon.screwbox.core.graphics.internal.renderer;

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

import java.awt.*;
import java.util.function.Supplier;

public class OffsetRenderer implements Renderer {

    private final Renderer next;
    private final Offset offset;

    public OffsetRenderer(final Offset offset, final Renderer next) {
        this.next = next;
        this.offset = offset;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        next.updateContext(graphics);
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        next.fillWith(color, clip);
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        next.fillWith(sprite, options.offset(options.offset().add(clip.offset())), clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        next.drawText(offset.add(clip.offset()), text, options, clip);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        next.drawRectangle(offset.add(clip.offset()), size, options, clip);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        next.drawLine(from.add(clip.offset()), to.add(clip.offset()), options, clip);
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        next.drawCircle(offset.add(clip.offset()), radius, options, clip);
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        next.drawSprite(sprite, origin.add(clip.offset()), options, clip);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        next.drawSprite(sprite, origin.add(clip.offset()), options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        next.drawText(offset.add(clip.offset()), text, options, clip);
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch, final ScreenBounds clip) {
        //TODO refactor into spritebatch.translate()
        SpriteBatch translatedSpriteBatch = new SpriteBatch();
        for (var entry : spriteBatch.entries()) {
            translatedSpriteBatch.add(entry.sprite(), entry.offset().add(clip.offset()), entry.options(), entry.drawOrder());
        }
        next.drawSpriteBatch(spriteBatch, clip);
    }
}
