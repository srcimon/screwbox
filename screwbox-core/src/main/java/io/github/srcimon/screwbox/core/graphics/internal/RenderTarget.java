package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sizeable;
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

public class RenderTarget implements Viewport {
    //TODO feature = reduce screen size within window
    private final Renderer renderer;
    private ScreenBounds clip;

    public RenderTarget(final Renderer renderer, final ScreenBounds clip) {
        this.renderer = renderer;
        this.clip = clip;
    }

    public void updateClip(final ScreenBounds clip) {
        this.clip = clip;
        //TODO when this is called every frame (not sure jet) make sure renderer is only changed on ofset change
    }

    public void fillWith(final Color color) {
        renderer.fillWith(color, clip);
    }

    public void fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.fillWith(sprite, options, clip);
    }

    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        renderer.drawText(offset, text, options, clip);
    }

    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderer.drawRectangle(offset, size, options, clip);
    }

    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from, to, options, clip);
    }

    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.drawCircle(offset, radius, options, clip);
    }

    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options, clip);
    }

    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options, clip);
    }

    public void drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.drawText(offset, text, options, clip);
    }

    public void drawSpriteBatch(final SpriteBatch spriteBatch) {
        renderer.drawSpriteBatch(spriteBatch, clip);
    }

    @Override
    public Size size() {
        return clip.size();
    }

    @Override
    public Offset offset() {
        return clip.offset();
    }

    public ScreenBounds screenBounds() {
        return clip;
    }

    @Override
    public Offset center() {
        return clip.center();
    }

    @Override
    public ScreenBounds bounds() {
        return clip;
    }
}
