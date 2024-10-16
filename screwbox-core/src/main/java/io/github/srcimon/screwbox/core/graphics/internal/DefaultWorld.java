package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

public class DefaultWorld implements World {

    private final DefaultCanvas defaultCanvas;

    private Vector cameraPosition = Vector.zero();
    private double zoom = 1;

    private Bounds visibleArea;

    public DefaultWorld(final DefaultCanvas defaultCanvas) {
        this.defaultCanvas = defaultCanvas;
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
                defaultCanvas.width() / zoom,
                defaultCanvas.height() / zoom);
    }

    @Override
    public Bounds visibleArea() {
        return visibleArea;
    }

    @Override
    public World drawRectangle(final Bounds bounds, final RectangleDrawOptions options) {
        final Offset offset = toCanvas(bounds.origin());
        final Size size = toDimension(bounds.size());
        defaultCanvas.drawRectangle(offset, size, options);
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final LineDrawOptions options) {
        defaultCanvas.drawLine(toCanvas(from), toCanvas(to), options);
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final double radius, final CircleDrawOptions options) {
        defaultCanvas.drawCircle(toCanvas(position), toDistance(radius), options);
        return this;
    }

    public Offset toScreen(final Vector position) {
        final double x = (position.x() - cameraPosition.x()) * zoom + (defaultCanvas.width() / 2.0);
        final double y = (position.y() - cameraPosition.y()) * zoom + (defaultCanvas.height() / 2.0);
        return Offset.at(x, y);
    }

    public Offset toCanvas(final Vector position) {
        return toScreen(position).add(defaultCanvas.offset());
    }

    public Vector toPosition(final Offset offset) {
        final double x = (offset.x() + defaultCanvas.offset().x() - (defaultCanvas.width() / 2.0)) / zoom + cameraPosition.x();
        final double y = (offset.y() + defaultCanvas.offset().y() - (defaultCanvas.height() / 2.0)) / zoom + cameraPosition.y();

        return Vector.of(x, y);
    }

    public Vector toRenderPositionByTargetSize(final Offset offset) {//TODO better name
        final double x = (offset.x() - (defaultCanvas.width() / 2.0)) / zoom + cameraPosition.x();
        final double y = (offset.y() - (defaultCanvas.height() / 2.0)) / zoom + cameraPosition.y();

        return Vector.of(x, y);
    }

    @Override
    public World drawText(final Vector position, final String text, final TextDrawOptions options) {
        defaultCanvas.drawText(toCanvas(position), text, options.scale(options.scale() * zoom));
        return this;
    }

    @Override
    public World drawText(final Vector position, final String text, final SystemTextDrawOptions options) {
        final Offset windowOffset = toCanvas(position);
        defaultCanvas.drawText(windowOffset, text, options);
        return this;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final SpriteDrawOptions options) {
        final SpriteDrawOptions scaledOptions = options.scale(options.scale() * zoom);
        defaultCanvas.drawSprite(sprite, toCanvas(origin), scaledOptions);
        return this;
    }

    private Size toDimension(final Vector size) {
        final long x = Math.round(size.x() * zoom);
        final long y = Math.round(size.y() * zoom);
        return Size.of(x, y);
    }

    public ScreenBounds toCanvas(final Bounds bounds) {
        final var offset = toCanvas(bounds.origin());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }

    public int toDistance(final double distance) {
        return (int) Math.round(distance * zoom);
    }

    public ScreenBounds toScreen(final Bounds bounds, final double parallaxX, final double parallaxY) {
        final Vector position = bounds.origin();
        final var offset = Offset.at(
                (position.x() - parallaxX * cameraPosition.x()) * zoom + (defaultCanvas.width() / 2.0) + defaultCanvas.offset().x(),
                (position.y() - parallaxY * cameraPosition.y()) * zoom + (defaultCanvas.height() / 2.0) + defaultCanvas.offset().y());
        final var size = toDimension(bounds.size());
        return new ScreenBounds(offset, size);
    }
}