package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.*;

public class DefaultWorld implements World {

    private final Screen screen;

    private Vector cameraPosition = Vector.zero();
    private double zoom = 1;

    private Bounds visibleArea;

    public DefaultWorld(final Screen screen) {
        this.screen = screen;
        recalculateVisibleArea();
    }

    public void updateZoom(final double zoom) {
        this.zoom = zoom;
        recalculateVisibleArea();
    }

    public void updateCameraPosition(final Vector position) {
        this.cameraPosition = position;
        recalculateVisibleArea();
    }

    public void recalculateVisibleArea() {
        visibleArea = Bounds.atPosition(cameraPosition,
                screen.width() / zoom,
                screen.height() / zoom);
    }

    @Override
    public Bounds visibleArea() {
        return visibleArea;
    }

    @Override
    public World drawRectangle(final Bounds bounds, final RectangleDrawOptions options) {
        final Offset offset = toOffset(bounds.origin());
        final Size size = toDimension(bounds.size());
        screen.drawRectangle(offset, size, options);
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final LineDrawOptions options) {
        screen.drawLine(toOffset(from), toOffset(to), options);
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final double radius, final CircleDrawOptions options) {
        screen.drawCircle(toOffset(position), toDistance(radius), options);
        return this;
    }

    public Offset toOffset(final Vector position) {
        final double x = (position.x() - cameraPosition.x()) * zoom + (screen.width() / 2.0);
        final double y = (position.y() - cameraPosition.y()) * zoom + (screen.height() / 2.0);
        return Offset.at(x, y);
    }

    public Vector toPosition(final Offset offset) {
        final double x = (offset.x() - (screen.width() / 2.0)) / zoom + cameraPosition.x();
        final double y = (offset.y() - (screen.height() / 2.0)) / zoom + cameraPosition.y();

        return Vector.of(x, y);
    }

    @Override
    public World drawText(final Vector position, final String text, final TextDrawOptions options) {
        screen.drawText(toOffset(position), text, options.scale(options.scale() * zoom));
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final SystemTextDrawOptions options) {
        final Offset windowOffset = toOffset(position);
        screen.drawText(windowOffset, text, options);
        return this;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final SpriteDrawOptions options) {
        final SpriteDrawOptions scaledOptions = options.scale(options.scale() * zoom);
        screen.drawSprite(sprite, toOffset(origin), scaledOptions);
        return this;
    }

    private Size toDimension(final Vector size) {
        final long x = Math.round(size.x() * zoom);
        final long y = Math.round(size.y() * zoom);
        return Size.of(x, y);
    }

    public ScreenBounds toScreen(final Bounds bounds) {
        final var offset = toOffset(bounds.origin());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    public int toDistance(final double distance) {
        return (int) Math.round(distance * zoom);
    }

    public ScreenBounds toScreen(final Bounds bounds, final double parallaxX, final double parallaxY) {
        final Vector position = bounds.origin();
        final var offset = Offset.at(
                (position.x() - parallaxX * cameraPosition.x()) * zoom + (screen.width() / 2.0),
                (position.y() - parallaxY * cameraPosition.y()) * zoom + (screen.height() / 2.0));
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }
}