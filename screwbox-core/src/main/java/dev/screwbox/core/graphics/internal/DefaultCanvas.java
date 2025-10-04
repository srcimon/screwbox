package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBatch;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;

import java.util.List;
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
    public Canvas drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {//TODO oval options rename
        return drawOval(offset, radius, radius, options);
    }

    @Override
    public Canvas drawOval(Offset offset, int radiusX, int radiusY, CircleDrawOptions options) {//TODO oval options rename
        renderer.drawCircle(this.offset.add(offset), radiusX, radiusY, options, clip);
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
        for (final var entry : spriteBatch.entriesInOrder()) {
            renderer.drawSprite(entry.sprite(), entry.offset().add(offset), entry.options(), clip);
        }
        return this;
    }

    @Override
    public Canvas drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options) {
        renderer.drawPolygon(nodes, options, clip);
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
