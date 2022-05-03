package de.suzufa.screwbox.core.graphics.world.internal;

import static de.suzufa.screwbox.core.graphics.window.WindowLine.line;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.window.Window;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.window.WindowRectangle;
import de.suzufa.screwbox.core.graphics.window.WindowSprite;
import de.suzufa.screwbox.core.graphics.window.WindowText;
import de.suzufa.screwbox.core.graphics.world.World;
import de.suzufa.screwbox.core.graphics.world.WorldLine;
import de.suzufa.screwbox.core.graphics.world.WorldPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldRectangle;
import de.suzufa.screwbox.core.graphics.world.WorldSprite;
import de.suzufa.screwbox.core.graphics.world.WorldText;

public class DefaultWorld implements World {

    private final Window window;

    private Vector cameraPosition = Vector.zero();
    private double zoom = 1;
    private Bounds visibleArea = Bounds.atOrigin(
            -Double.MAX_VALUE / 2,
            -Double.MAX_VALUE / 2,
            Double.MAX_VALUE,
            Double.MAX_VALUE);

    public DefaultWorld(final Window window) {
        this.window = window;
    }

    public double updateCameraZoom(final double zoom) {
        final double actualZoomValue = Math.floor(zoom * 16.0) / 16.0;
        this.zoom = actualZoomValue;
        this.visibleArea = calculateVisibleArea();
        return actualZoomValue;
    }

    public void updateCameraPosition(final Vector position) {
        this.cameraPosition = position;
        this.visibleArea = calculateVisibleArea();
    }

    public Vector cameraPosition() {
        return cameraPosition;
    }

    @Override
    public void draw(final WorldSprite sprite) {
        window.draw(worldToScreen(sprite));
    }

    @Override
    public void draw(final WorldRectangle rectangle) {
        if (!rectangle.bounds().intersects(visibleArea)) {
            return;
        }

        window.draw(worldToScreen(rectangle));
    }

    @Override
    public void draw(final WorldLine line) {
        window.draw(worldToScreen(line));
    }

    @Override
    public void draw(final WorldText text) {
        // TODO: check if intersects camera bounds
        window.draw(worldToScreen(text));
    }

    @Override
    public void draw(final WorldPolygon polygon) {
        window.draw(worldToScreen(polygon));

    }

    @Override
    public Bounds visibleArea() {
        return visibleArea;
    }

    public double cameraZoom() {
        return zoom;
    }

    public Offset toOffset(final Vector position) {
        final double x = (position.x() - cameraPosition.x()) * zoom + (window.size().width() / 2.0);
        final double y = (position.y() - cameraPosition.y()) * zoom + (window.size().height() / 2.0);
        return Offset.at(x, y);
    }

    public Vector toPosition(final Offset offset) {
        final double x = (offset.x() - (window.size().width() / 2.0)) / zoom + cameraPosition.x();
        final double y = (offset.y() - (window.size().height() / 2.0)) / zoom + cameraPosition.y();

        return Vector.of(x, y);
    }

    private Bounds calculateVisibleArea() {
        return Bounds.atPosition(cameraPosition,
                window.size().width() / zoom,
                window.size().height() / zoom);
    }

    private Dimension toDimension(final Vector size) {
        final long x = Math.round(size.x() * zoom);
        final long y = Math.round(size.y() * zoom);
        return Dimension.of(x, y);
    }

    private WindowPolygon worldToScreen(final WorldPolygon polygon) {
        final List<Offset> offsets = new ArrayList<>();
        for (final var point : polygon.points()) {
            offsets.add(toOffset(point));
        }
        return WindowPolygon.polygon(offsets, polygon.color(), polygon.opacity());
    }

    private WindowText worldToScreen(final WorldText text) {
        return new WindowText(toOffset(text.position()), text.text(), text.font(), text.color(),
                text.opacity(), text.centered());
    }

    private WindowSprite worldToScreen(final WorldSprite sprite) {
        final var offset = toOffset(sprite.position());
        return WindowSprite.sprite(sprite.sprite(), offset, zoom, sprite.opacity(), sprite.rotation());
    }

    private WindowRectangle worldToScreen(final WorldRectangle rectangle) {
        final Offset offset = toOffset(rectangle.bounds().origin());
        final Dimension dimension = toDimension(rectangle.bounds().size());
        return WindowRectangle.rectangle(offset, dimension, rectangle.color());
    }

    private WindowLine worldToScreen(final WorldLine line) {
        return line(toOffset(line.from()), toOffset(line.to()), line.color(), line.opacity());
    }

}
