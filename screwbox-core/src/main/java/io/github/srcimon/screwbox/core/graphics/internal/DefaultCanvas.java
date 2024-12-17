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

import java.util.function.Supplier;

public class DefaultCanvas implements Canvas {

    private final Renderer renderer;
    private ScreenBounds clip;
    private Offset offset;
    private ScreenBounds visibleArea;

    public DefaultCanvas(final Renderer renderer, final ScreenBounds clip) {
        this.renderer = renderer;
        updateClip(clip);
    }

    //TODO Test
    @Override
    public Canvas subcanvas(final ScreenBounds clip) {
        if (!this.clip.contains(clip)) {
            throw new IllegalArgumentException("new clip cannot be outside of canvas");
        }
        return new DefaultCanvas(renderer, clip);
    }

    public void updateClip(final ScreenBounds clip) {
        this.clip = clip;
        this.offset = clip.offset();
        this.visibleArea = new ScreenBounds(size());
    }

    @Override
    public Canvas fillWith(final Color color) {
        renderer.fillWith(color, clip);
        return this;
    }

    @Override
    public Canvas fillWith(final Sprite sprite, final SpriteFillOptions options) {
        renderer.fillWith(sprite, options.offset(options.offset().add(offset)), clip);
        return this;
    }

    @Override
    public Canvas drawText(final Offset offset, final String text, final SystemTextDrawOptions options) {
        renderer.drawText(this.offset.add(offset), text, options, clip);
        return this;
    }

    @Override
    public Canvas drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderer.drawRectangle(this.offset.add(offset), size, options, clip);
        return this;
    }

    @Override
    public Canvas drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from.add(offset), to.add(offset), options, clip);
        return this;
    }

    @Override
    public Canvas drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.drawCircle(this.offset.add(offset), radius, options, clip);
        return this;
    }

    @Override
    public Canvas drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin.add(offset), options, clip);
        return this;
    }

    @Override
    public Canvas drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options) {
        renderer.drawSprite(sprite, origin.add(offset), options, clip);
        return this;
    }

    @Override
    public Canvas drawText(final Offset offset, final String text, final TextDrawOptions options) {
        renderer.drawText(offset.add(this.offset), text, options, clip);
        return this;
    }

    @Override
    public Canvas drawSpriteBatch(final SpriteBatch spriteBatch) {
        renderer.drawSpriteBatch(spriteBatch.translate(offset), clip);
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

    @Override
    public boolean isVisible(final ScreenBounds other) {
        return visibleArea.intersects(other);
    }
}
