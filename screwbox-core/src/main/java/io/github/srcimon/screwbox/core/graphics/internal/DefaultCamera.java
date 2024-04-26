package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.CameraShakeOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultCamera implements Camera, Updatable {

    private final DefaultWorld world;
    private Vector shake = Vector.zero();
    private Vector position = Vector.zero();
    private double zoom = 2;
    private double requestedZoom = zoom;
    private double minZoom = 1;
    private double maxZoom = 5;

    private ActiveCameraShake activeShake;

    public DefaultCamera(final DefaultWorld world) {
        this.world = world;
    }

    @Override
    public Camera setPosition(final Vector position) {
        this.position = requireNonNull(position, "position must not be NULL");
        world.updateCameraPosition(focus());
        return this;
    }

    @Override
    public Vector position() {
        return position;
    }

    @Override
    public Vector focus() {
        final var focus = this.position.add(shake);
        return Vector.of(pixelPerfectValue(focus.x()), pixelPerfectValue(focus.y()));
    }

    @Override
    public Camera setZoomRestriction(final double min, final double max) {
        if (min <= 0) {
            throw new IllegalArgumentException("min zoom must be positive");
        }
        if (min > max) {
            throw new IllegalArgumentException("max zoom must not be lower than min zoom");
        }
        this.minZoom = min;
        this.maxZoom = max;
        return this;
    }

    @Override
    public double setZoom(final double zoom) {
        this.requestedZoom = Math.clamp(zoom, minZoom, maxZoom);
        this.zoom = pixelPerfectValue(requestedZoom);
        world.updateZoom(this.zoom);
        return this.zoom;
    }

    @Override
    public Vector moveWithinVisualBounds(final Vector delta, final Bounds bounds) {
        final var legalPostionArea = Bounds.atPosition(bounds.position(),
                Math.max(1, bounds.width() - world.visibleArea().width()),
                Math.max(1, bounds.height() - world.visibleArea().height()));

        final double movementX = Math.clamp(delta.x(), legalPostionArea.minX() - position().x(), legalPostionArea.maxX() - position().x());

        final double movementY = Math.clamp(delta.y(), legalPostionArea.minY() - position().y(), legalPostionArea.maxY() - position().y());

        move($(movementX, movementY));
        return position();
    }

    @Override
    public double changeZoomBy(final double delta) {
        return setZoom(requestedZoom + delta);
    }

    @Override
    public Camera shake(CameraShakeOptions options) {
        activeShake = new ActiveCameraShake(options);
        return this;
    }

    @Override
    public Camera stopShaking() {
        activeShake = null;
        return this;
    }

    @Override
    public boolean isShaking() {
        return nonNull(activeShake);
    }

    @Override
    public double zoom() {
        return zoom;
    }

    @Override
    public double minZoom() {
        return minZoom;
    }

    @Override
    public double maxZoom() {
        return maxZoom;
    }

    @Override
    public void update() {
        Time now = Time.now();

        if (nonNull(activeShake)) {
            shake = activeShake.calculateDistortion(now, zoom);
            if (activeShake.hasEnded(now)) {
                activeShake = null;
            }
        } else {
            shake = Vector.zero();
        }
    }

    private double pixelPerfectValue(final double value) {
        return Math.floor(value * 16.0) / 16.0;
    }
}
