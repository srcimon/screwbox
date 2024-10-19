package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Viewport;

public class DefaultViewport implements Viewport {

    private final Canvas canvas;
    private final Camera camera;

    public DefaultViewport(final Canvas canvas, final Camera camera) {
        this.canvas = canvas;
        this.camera = camera;
    }

    @Override
    public Offset toCanvas(final Vector position) {
        final double x = (position.x() - camera.focus().x()) * camera.zoom() + (canvas.width() / 2.0) + canvas.offset().x();
        final double y = (position.y() - camera.focus().y()) * camera.zoom() + (canvas.height() / 2.0) + canvas.offset().y();
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
        final double x = (offset.x() - (canvas.width() / 2.0)) / camera.zoom() + camera.focus().x();
        final double y = (offset.y() - (canvas.height() / 2.0)) / camera.zoom() + camera.focus().y();

        return Vector.of(x, y);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds) {
        final var offset = toCanvas(bounds.origin());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    @Override
    public int toCanvas(final double worldDistance) {
        return (int) Math.round(worldDistance * camera.zoom());
    }

    @Override
    public ScreenBounds toCanvas(Bounds bounds, double parallaxX, double parallaxY) {
        final Vector position = bounds.origin();
        final var offset = Offset.at(
                (position.x() - parallaxX * camera.focus().x()) * camera.zoom() + (canvas.width() / 2.0) + canvas.offset().x(),
                (position.y() - parallaxY * camera.focus().y()) * camera.zoom() + (canvas.height() / 2.0) + canvas.offset().y());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    @Override
    public Camera camera() {
        return camera;
    }

    @Override
    public Canvas canvas() {
        return canvas;
    }

    private Size toDimension(final Vector size) {
        final long x = Math.round(size.x() * camera.zoom());
        final long y = Math.round(size.y() * camera.zoom());
        return Size.of(x, y);
    }
}
