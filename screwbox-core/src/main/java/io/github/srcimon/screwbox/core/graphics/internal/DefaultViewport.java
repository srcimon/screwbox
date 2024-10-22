package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;

public record DefaultViewport(Canvas canvas, Camera camera) implements Viewport {

    @Override
    public Offset toCanvas(final Vector position) {
        final double x = (position.x() - camera.focus().x()) * camera.zoom() + (canvas.width() / 2.0);
        final double y = (position.y() - camera.focus().y()) * camera.zoom() + (canvas.height() / 2.0);
        return Offset.at(x, y);
    }

    @Override
    public Bounds visibleArea() {
        return Bounds.atPosition(camera.focus(),
                canvas.width() / camera.zoom(),
                canvas.height() / camera.zoom());
    }

    @Override
    public Vector toWorld(final Offset offset) {
        final double x = (canvas.width() / 2.0) / camera.zoom() + camera.focus().x();
        final double y = (canvas.height() / 2.0) / camera.zoom() + camera.focus().y();

        return Vector.of(x, y);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds) {
        final var offset = toCanvas(bounds.origin());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    @Override
    public int toCanvas(final double distance) {
        return (int) Math.round(distance * camera.zoom());
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds, final double parallaxX, final double parallaxY) {
        final var position = bounds.origin();
        final var offset = Offset.at(
                (position.x() - parallaxX * camera.focus().x()) * camera.zoom() + (canvas.width() / 2.0),
                (position.y() - parallaxY * camera.focus().y()) * camera.zoom() + (canvas.height() / 2.0));
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    private Size toDimension(final Vector size) {
        return Size.of(toCanvas(size.x()), toCanvas(size.y()));
    }
}
