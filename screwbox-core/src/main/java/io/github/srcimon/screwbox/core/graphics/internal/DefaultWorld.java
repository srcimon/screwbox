package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

public class DefaultWorld implements World {

    private final ViewportManager viewportManager;

    public DefaultWorld(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public World drawRectangle(final Bounds bounds, final RectangleDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            final var canvasBounds = viewport.toCanvas(bounds);
            viewport.canvas().drawRectangle(canvasBounds, options);
        }
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final LineDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            viewport.canvas().drawLine(viewport.toCanvas(from), viewport.toCanvas(to), options);
        }
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final double radius, final CircleDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            viewport.canvas().drawCircle(viewport.toCanvas(position), viewport.toCanvas(radius), options);
        }
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final TextDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            viewport.canvas().drawText(viewport.toCanvas(position), text, options.scale(options.scale() * viewport.camera().zoom()));
        }
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final SystemTextDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            final Offset windowOffset = viewport.toCanvas(position);
            viewport.canvas().drawText(windowOffset, text, options);
        }
        return this;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final SpriteDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            final SpriteDrawOptions scaledOptions = options.scale(options.scale() * viewport.camera().zoom());
            viewport.canvas().drawSprite(sprite, viewport.toCanvas(origin), scaledOptions);
        }
        return this;
    }

}