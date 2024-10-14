package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sizeable;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.OffsetTranslatingRenderer;

import java.util.function.Supplier;

public class Rendertarget implements Sizeable {
    //TODO feature = reduce screen size within window
    private final Renderer renderer;
    private Renderer usedRenderer;
    private ScreenBounds clip = new ScreenBounds(0, 0, 4, 4);//TODO initialize better

    public Rendertarget(final Renderer renderer) {
        this.renderer = renderer;
    }

    public void updateClip(final ScreenBounds clip) {
        this.clip = clip;
        //TODO when this is called every frame (not sure jet) make sure renderer is only changed on ofset change
        usedRenderer = Offset.origin().equals(clip.offset())
                ? renderer
                : new OffsetTranslatingRenderer(clip.offset(), renderer);
    }

    public void fillWith(final Color color) {
        usedRenderer.fillWith(color, clip);
    }

    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        usedRenderer.fillWith(sprite, options, clip);
    }

    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        usedRenderer.drawText(offset, text, options, clip);
    }

    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        usedRenderer.drawRectangle(offset, size, options, clip);
    }

    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        usedRenderer.drawLine(from, to, options, clip);
    }

    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        usedRenderer.drawCircle(offset, radius, options, clip);
    }

    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        usedRenderer.drawSprite(sprite, origin, options, clip);
    }

    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        usedRenderer.drawSprite(sprite, origin, options, clip);
    }

    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        usedRenderer.drawText(offset, text, options, clip);
    }

    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        usedRenderer.drawSpriteBatch(spriteBatch, clip);
    }

    @Override
    public Size size() {
        return clip.size();
    }

    //TODO Positionable interface .x(), .y()
    public Offset offset() {
        return clip.offset();
    }

    public ScreenBounds screenBounds() {
        return clip;
    }
}
