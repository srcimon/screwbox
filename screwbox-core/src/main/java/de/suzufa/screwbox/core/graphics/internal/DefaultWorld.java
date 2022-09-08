package de.suzufa.screwbox.core.graphics.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.SpriteBatch;
import de.suzufa.screwbox.core.graphics.SpriteBatch.SpriteBatchEntry;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;

public class DefaultWorld implements World {

    private final Window window;

    private Vector cameraPosition = Vector.zero();
    private double zoom = 1;
    private Bounds visibleArea = Bounds.atOrigin(
            -Double.MAX_VALUE / 2,
            -Double.MAX_VALUE / 2,
            Double.MAX_VALUE,
            Double.MAX_VALUE);

    private Color drawColor = Color.WHITE;

    public DefaultWorld(final Window window) {
        this.window = window;
    }

    @Override
    public World drawColor(final Color color) {
        this.drawColor = color;
        return this;
    }

    @Override
    public Color drawColor() {
        return drawColor;
    }

    public double updateCameraZoom(final double zoom) {
        final double actualZoomValue = Math.floor(zoom * 16.0) / 16.0;
        this.zoom = actualZoomValue;
        recalculateVisibleArea();
        return actualZoomValue;
    }

    public void updateCameraPosition(final Vector position) {
        this.cameraPosition = position;
        recalculateVisibleArea();
    }

    public void recalculateVisibleArea() {
        this.visibleArea = Bounds.atPosition(cameraPosition,
                window.size().width() / zoom,
                window.size().height() / zoom);
    }

    public Vector cameraPosition() {
        return cameraPosition;
    }

    @Override
    public World drawSprite(final Sprite sprite, final Vector origin, final double scale, final Percentage opacity,
            final Rotation rotation, final FlipMode flipMode) {
        final var offset = toOffset(origin);
        final var x = offset.x() - ((scale - 1) * sprite.size().width());
        final var y = offset.y() - ((scale - 1) * sprite.size().height());
        window.drawSprite(sprite, Offset.at(x, y), scale * zoom, opacity, rotation, flipMode);
        return this;
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

    private Dimension toDimension(final Vector size) {
        final long x = Math.round(size.x() * zoom);
        final long y = Math.round(size.y() * zoom);
        return Dimension.of(x, y);
    }

    @Override
    public World drawText(final Vector offset, final String text, final Font font, final Color color) {
        final Offset windowOffset = toOffset(offset);
        window.drawText(windowOffset, text, font, color);
        return this;
    }

    @Override
    public World drawTextCentered(final Vector position, final String text, final Font font, final Color color) {
        final Offset offset = toOffset(position);
        window.drawTextCentered(offset, text, font, color);
        return this;
    }

    @Override
    public World drawLine(final Vector from, final Vector to, final Color color) {
        window.drawLine(toOffset(from), toOffset(to), color);
        return this;
    }

    @Override
    public World drawPolygon(final List<Vector> points, final Color color) {
        final List<Offset> offsets = new ArrayList<>();
        for (final var point : points) {
            offsets.add(toOffset(point));
        }
        window.drawPolygon(offsets, color);
        return this;
    }

    @Override
    public World drawCircle(final Vector position, final int diameter, final Color color) {
        final Offset offset = toOffset(position);
        window.drawCircle(offset, (int) (diameter * zoom), color);
        return this;
    }

    @Override
    public World drawRectangle(final Bounds bounds, final Color color) {
        final Offset offset = toOffset(bounds.origin());
        final Dimension size = toDimension(bounds.size());
        window.drawRectangle(offset, size, color);
        return this;
    }

    @Override
    public World drawTextCentered(final Vector position, final String text, final Pixelfont font,
            final Percentage opacity, final double scale) {
        final Offset offset = toOffset(position);
        window.drawTextCentered(offset, text, font, opacity, scale * zoom);
        return this;
    }

    @Override
    public World drawSpriteBatch(final SpriteBatch spriteBatch) {
        for (final SpriteBatchEntry entry : spriteBatch.entriesInDrawOrder()) {
            drawSprite(entry.sprite(),
                    entry.position(),
                    entry.scale(),
                    entry.opacity(),
                    entry.rotation(),
                    entry.flipMode());
        }
        return this;
    }

}
