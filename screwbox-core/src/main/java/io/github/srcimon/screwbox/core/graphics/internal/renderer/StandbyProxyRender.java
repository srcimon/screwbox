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

//TODO move splitscreen compensation to another batch
public class StandbyProxyRender implements Renderer {

    private Renderer next;
    private final Renderer actualNext;

    public StandbyProxyRender(final Renderer next) {
        this.next = next;
        this.actualNext = next;
    }

    public void standBy() {
        next = new StandByRenderer();
    }

    public void wakeUp() {
        next = actualNext;
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
        next.fillWith(sprite, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = offset.add(clip.offset());
        next.drawText(actualOffset, text, options, clip);
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = offset.add(clip.offset());
        next.drawRectangle(actualOffset, size, options, clip);
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        next.drawLine(from.add(clip.offset()), to.add(clip.offset()), options, clip);
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = offset.add(clip.offset());
        next.drawCircle(actualOffset, radius, options, clip);
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = origin.add(clip.offset());
        next.drawSprite(sprite, actualOffset, options, clip);
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = origin.add(clip.offset());
        next.drawSprite(sprite, actualOffset, options, clip);
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        final Offset actualOffset = offset.add(clip.offset());
        next.drawText(actualOffset, text, options, clip);
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch, final ScreenBounds clip) {
        if (clip.offset().equals(Offset.origin())) {
            next.drawSpriteBatch(spriteBatch, clip);
        } else {
            SpriteBatch actualBatch = new SpriteBatch();
            for (var e : spriteBatch.entries()) {
                actualBatch.add(e.sprite(), clip.offset().add(e.offset()), e.options(), e.drawOrder());
            }
            next.drawSpriteBatch(actualBatch, clip);
        }
    }
}
