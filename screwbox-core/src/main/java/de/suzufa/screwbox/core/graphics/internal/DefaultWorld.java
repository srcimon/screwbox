package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.utils.MathUtil.clamp;
import static java.util.Objects.isNull;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
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
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.graphics.World;

public class DefaultWorld implements World {

    private final Window window;

    private Vector cameraPosition = Vector.zero();
    private double zoom = 1;
    private double wantedZoom = zoom;
    private double minZoom = 0.5;
    private double maxZoom = 10;

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

    public void restrictZoomRangeTo(final double min, final double max) {
        if (min <= 0) {
            throw new IllegalArgumentException("min zoom must be positive");
        }
        if (min > max) {
            throw new IllegalArgumentException("max zoom must not be lower than min zoom");
        }
        this.minZoom = min;
        this.maxZoom = max;
    }

    public double wantedZoom() {
        return wantedZoom;
    }

    public double updateCameraZoom(final double zoom) {
        this.wantedZoom = clamp(minZoom, zoom, maxZoom);
        final double actualZoomValue = Math.floor(wantedZoom * 16.0) / 16.0;
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
    public World drawSprite(final Sprite sprite, final Vector origin, final double scale, final Percent opacity,
            final Angle rotation, final FlipMode flipMode, final Bounds clipArea) {
        final var offset = toOffset(origin);
        final var windowClipArea = isNull(clipArea) ? null : toWindowBounds(clipArea);
        final var x = offset.x() - ((scale - 1) * sprite.size().width());
        final var y = offset.y() - ((scale - 1) * sprite.size().height());
        window.drawSprite(sprite, Offset.at(x, y), scale * zoom, opacity, rotation, flipMode, windowClipArea);
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
            final Percent opacity, final double scale) {
        final Offset offset = toOffset(position);
        window.drawTextCentered(offset, text, font, opacity, scale * zoom);
        return this;
    }

    @Override
    public World drawSpriteBatch(final SpriteBatch spriteBatch, final Bounds clipArea) {
        for (final SpriteBatchEntry entry : spriteBatch.entriesInDrawOrder()) {
            drawSprite(entry.sprite(),
                    entry.position(),
                    entry.scale(),
                    entry.opacity(),
                    entry.rotation(),
                    entry.flipMode(),
                    clipArea);
        }
        return this;
    }

    private Dimension toDimension(final Vector size) {
        final long x = Math.round(size.x() * zoom);
        final long y = Math.round(size.y() * zoom);
        return Dimension.of(x, y);
    }

    public WindowBounds toWindowBounds(final Bounds bounds) {
        final var offset = toOffset(bounds.origin());
        final var size = toDimension(bounds.size());
        return new WindowBounds(offset, size);
    }

    public int toDistance(double distance) {
        return (int) Math.round(distance * zoom);
    }

    @Override
    public World drawFadingCircle(Vector position, double diameter, Color color) {
        window.drawFadingCircle(toOffset(position), toDistance(diameter), color);
        return this;
    }
}