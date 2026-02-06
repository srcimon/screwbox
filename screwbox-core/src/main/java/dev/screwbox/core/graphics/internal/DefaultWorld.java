package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;

import java.util.ArrayList;
import java.util.List;

public class DefaultWorld implements World {

    private final ViewportManager viewportManager;

    public DefaultWorld(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public World drawRectangle(final Bounds bounds, final RectangleDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            final var canvasBounds = viewport.toCanvas(bounds);
            viewport.canvas().drawRectangle(canvasBounds, options.curveRadius(viewport.toCanvas(options.curveRadius())));
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
    public World drawOval(final Vector position, final double radiusX, final double radiusY, final OvalDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            viewport.canvas().drawOval(viewport.toCanvas(position), viewport.toCanvas(radiusX), viewport.toCanvas(radiusY), options);
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
    public World drawPolygon(final List<Vector> nodes, final PolygonDrawOptions options) {
        for (final var viewport : viewportManager.viewports()) {
            final List<Offset> translatedNodes = new ArrayList<>();
            for (final var node : nodes) {
                translatedNodes.add(viewport.toCanvas(node));
            }
            viewport.canvas().drawPolygon(translatedNodes, options);
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