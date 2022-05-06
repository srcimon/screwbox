package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.window.WindowLine.line;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.graphics.window.WindowLine;
import de.suzufa.screwbox.core.graphics.window.WindowPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldLine;
import de.suzufa.screwbox.core.graphics.world.WorldPolygon;
import de.suzufa.screwbox.core.graphics.world.WorldRectangle;
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

    private Color defaultDrawingColor;

    public DefaultWorld(final Window window) {
        this.window = window;
    }

    @Override
    public World setDrawingColor(final Color color) {
        this.defaultDrawingColor = color;
        return this;
    }

    @Override
    public Color drawingColor() {
        return defaultDrawingColor;
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
    public World drawSprite(final Sprite sprite, final Vector origin, final double scale, final Percentage opacity,
            final Rotation rotation) {
        final var offset = toOffset(origin);
        window.drawSprite(sprite, offset, scale * zoom, opacity, rotation);
        return this;
    }

    @Override
    public void draw(final WorldRectangle rectangle) {
        if (!rectangle.bounds().intersects(visibleArea)) {// TODO: REMOVE ALL CHECKS FROM RENDERER/GRAPHICS
            return;
        }

        final Offset offset = toOffset(rectangle.bounds().origin());
        final Dimension dimension = toDimension(rectangle.bounds().size());

        window.drawRectangle(offset, dimension, rectangle.color());
    }

    @Override
    public void draw(final WorldLine line) {
        window.draw(worldToScreen(line));
    }

    @Override
    public void draw(final WorldText text) {
        final Offset offset = toOffset(text.position());
        if (text.centered()) {
            window.drawTextCentered(offset, text.text(), text.font(), text.color());
        } else {
            window.drawText(offset, text.text(), text.font(), text.color());
        }
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
        return WindowPolygon.polygon(offsets, polygon.color());
    }

    private WindowLine worldToScreen(final WorldLine line) {
        return line(toOffset(line.from()), toOffset(line.to()), line.color());
    }

}
