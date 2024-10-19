package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
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
        final var screenBounds = viewport.toCanvas(bounds);
        canvas.drawRectangle(screenBounds.offset(), screenBounds.size(), options);
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final LineDrawOptions options) {
        canvas.drawLine(viewport.toCanvas(from), viewport.toCanvas(to), options);
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final double radius, final CircleDrawOptions options) {
        canvas.drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options);
        return this;
    }

    //TODO move viewport dependency to mouse
    public Vector canvasToWorld(final Offset offset) {
        return viewport.toWorld(offset);
    }

    @Override
    public World drawText(final Vector position, final String text, final TextDrawOptions options) {
        canvas.drawText(viewport.toCanvas(position), text, options.scale(options.scale() * viewport.camera().zoom()));
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final SystemTextDrawOptions options) {
        final Offset windowOffset = viewport.toCanvas(position);
        canvas.drawText(windowOffset, text, options);
        return this;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final SpriteDrawOptions options) {
        final SpriteDrawOptions scaledOptions = options.scale(options.scale() * viewport.camera().zoom());
        canvas.drawSprite(sprite, viewport.toCanvas(origin), scaledOptions);
        return this;
    }

}