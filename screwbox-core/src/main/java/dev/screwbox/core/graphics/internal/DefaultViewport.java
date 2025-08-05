package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;

public record DefaultViewport(DefaultCanvas canvas, Camera camera) implements Viewport {

    public void updateClip(final ScreenBounds clip) {
        this.canvas.updateClip(clip);
    }

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
        final double x = (offset.x() - canvas.width() / 2.0) / camera.zoom() + camera.focus().x();
        final double y = (offset.y() - canvas.height() / 2.0) / camera.zoom() + camera.focus().y();

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
//TODO performance bottleneck
    @Override
    public ScreenBounds toCanvas(final Bounds bounds, final double parallaxX, final double parallaxY) {
        final var position = bounds.origin();
        final var focus = camera.focus();//TODO save focus for later
        final var offset = Offset.at(
                (position.x() - parallaxX * focus.x()) * camera.zoom() + (canvas.width() / 2.0),
                (position.y() - parallaxY * focus.y()) * camera.zoom() + (canvas.height() / 2.0));
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    private Size toDimension(final Vector size) {
        return Size.of(toCanvas(size.x()), toCanvas(size.y()));
    }
}
