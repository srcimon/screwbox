package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultCamera implements Camera, Updatable {

    private final Canvas canvas;
    private Vector shake = Vector.zero();
    private Vector position = Vector.zero();
    private double zoom = 1;
    private double requestedZoom = zoom;
    private double minZoom = 1;
    private double maxZoom = 5;
    private Rotation swing;

    private ActiveCameraShake activeShake;

    public DefaultCamera(final Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public Camera setPosition(final Vector position) {
        this.position = requireNonNull(position, "position must not be NULL");
        return this;
    }

    @Override
    public Vector position() {
        return position;
    }

    @Override
    public Vector focus() {
        final var focus = this.position.add(shake);
        return Pixelperfect.vector(focus);
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
        this.zoom = Pixelperfect.value(requestedZoom);
        return this.zoom;
    }

    @Override
    public Vector moveWithinVisualBounds(final Vector delta, final Bounds bounds) {
        final var legalPostionArea = Bounds.atPosition(bounds.position(),
                Math.max(1, bounds.width() - visibleArea().width()),
                Math.max(1, bounds.height() - visibleArea().height()));

        final double movementX = Math.clamp(delta.x(), legalPostionArea.minX() - position().x(), legalPostionArea.maxX() - position().x());

        final double movementY = Math.clamp(delta.y(), legalPostionArea.minY() - position().y(), legalPostionArea.maxY() - position().y());

        move($(movementX, movementY));
        return position();
    }

    private Bounds visibleArea() {
        return Bounds.atPosition(focus(),
                canvas.width() / zoom(),
                canvas.height() / zoom());
    }

    @Override
    public double changeZoomBy(final double delta) {
        return setZoom(requestedZoom + delta);
    }

    @Override
    public Camera shake(final CameraShakeOptions options) {
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
    public Rotation swing() {
        return swing;
    }

    @Override
    public void update() {
        final Time now = Time.now();
        if (nonNull(activeShake)) {
            shake = activeShake.calculateDistortion(now, zoom);
            swing = activeShake.caclulateSwing(now);
            if (activeShake.hasEnded(now)) {
                activeShake = null;
            }
        } else {
            shake = Vector.zero();
            swing = Rotation.none();
        }
    }
}
