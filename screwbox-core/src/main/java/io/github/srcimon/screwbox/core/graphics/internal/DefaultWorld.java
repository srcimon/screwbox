package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

public class DefaultWorld implements World {

    private final Canvas canvas;
    private final Viewport viewport;

    public DefaultWorld(final Canvas canvas, final Viewport viewport) {
        this.canvas = canvas;
        this.viewport = viewport;
    }

    @Override
    public Bounds visibleArea() {
        return viewport.visibleArea();
    }

    @Override
    public World drawRectangle(final Bounds bounds, final RectangleDrawOptions options) {
        var sb = toCanvas(bounds);
        canvas.drawRectangle(sb.offset(), sb.size(), options);
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final LineDrawOptions options) {
        canvas.drawLine(toCanvas(from), toCanvas(to), options);
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final double radius, final CircleDrawOptions options) {
        canvas.drawCircle(toCanvas(position), toDistance(radius), options);
        return this;
    }

    public Offset toCanvas(final Vector position) {
        return viewport.toCanvas(position);
    }

    public Vector toPosition(final Offset offset) {
        return viewport.toWorld(offset);
    }

    public Vector canvasToWorld(final Offset offset) {
        return viewport.toWorld(offset);
    }

    @Override
    public World drawText(final Vector position, final String text, final TextDrawOptions options) {
        canvas.drawText(toCanvas(position), text, options.scale(options.scale() * viewport.camera().zoom()));
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final SystemTextDrawOptions options) {
        final Offset windowOffset = toCanvas(position);
        canvas.drawText(windowOffset, text, options);
        return this;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final SpriteDrawOptions options) {
        final SpriteDrawOptions scaledOptions = options.scale(options.scale() * viewport.camera().zoom());
        canvas.drawSprite(sprite, toCanvas(origin), scaledOptions);
        return this;
    }

    //TODO move to viewport
    private Size toDimension(final Vector size) {
        final long x = Math.round(size.x() * viewport.camera().zoom());
        final long y = Math.round(size.y() * viewport.camera().zoom());
        return Size.of(x, y);
    }

    public ScreenBounds toCanvas(final Bounds bounds) {
        return viewport.toCanvas(bounds);
    }

    public int toDistance(final double distance) {
        return viewport.toCanvas(distance);
    }

    public ScreenBounds toScreen(final Bounds bounds, final double parallaxX, final double parallaxY) {
       return viewport.toCanvas(bounds, parallaxX, parallaxY);
    }
}