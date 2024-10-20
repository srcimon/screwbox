package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Canvas;
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
import io.github.srcimon.screwbox.core.graphics.internal.renderer.OffsetRenderer;

import java.util.function.Supplier;

public class DefaultCanvas implements Canvas {

    private final Renderer origRenderer;
    private Renderer renderer;
    private ScreenBounds clip;

    public DefaultCanvas(final Renderer renderer, final ScreenBounds clip) {
        this.origRenderer = renderer;
        updateClip(clip);
    }

    public void updateClip(final ScreenBounds clip) {
        this.clip = clip;
        this.renderer = new OffsetRenderer(clip.offset(), origRenderer);
    }

    @Override
    public Canvas fillWith(final Color color) {
        renderer.fillWith(color, clip);
        return this;
    }

    @Override
    public Canvas fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.fillWith(sprite, options, clip);
        return this;
    }

    @Override
    public Canvas drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        renderer.drawText(offset, text, options, clip);
        return this;
    }

    @Override
    public Canvas drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderer.drawRectangle(offset, size, options, clip);
        return this;
    }

    @Override
    public Canvas drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from, to, options, clip);
        return this;
    }

    @Override
    public Canvas drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.drawCircle(offset, radius, options, clip);
        return this;
    }

    @Override
    public Canvas drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options, clip);
        return this;
    }

    @Override
    public Canvas drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin, options, clip);
        return this;
    }

    @Override
    public Canvas drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.drawText(offset, text, options, clip);
        return this;
    }

    @Override
    public Canvas drawSpriteBatch(final SpriteBatch spriteBatch) {
        renderer.drawSpriteBatch(spriteBatch, clip);
        return this;
    }

    @Override
    public Size size() {
        return clip.size();
    }

    @Override
    public Offset offset() {
        return clip.offset();
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
