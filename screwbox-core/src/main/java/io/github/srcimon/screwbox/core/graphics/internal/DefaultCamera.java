package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.CameraShake;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import static io.github.srcimon.screwbox.core.Vector.$;

public class DefaultCamera implements Camera, Updatable {

    private final DefaultWorld world;
    private Vector shake = Vector.zero();
    Vector position = Vector.zero();
    private double zoom = 2;
    private double requestedZoom = zoom;
    private double minZoom = 2;
    private double maxZoom = 5;
    private Time start = null;
    private CameraShake activeShake = null;

    public DefaultCamera(DefaultWorld world) {
        this.world = world;
    }

    //TODO CameraShakeOptions...

    //TODO ADD ZOOM TO shake
    @Override
    public Camera updatePosition(Vector position) {
        this.position = position;
        world.updateCameraPosition(position);
        return this;
    }

    @Override
    public Vector position() {
        return position;
    }

    @Override
    public Vector focus() {
        return position.add(shake);
    }

    @Override
    public Camera updateZoomRestriction(final double min, final double max) {
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
    public double updateZoom(final double zoom) {
        this.requestedZoom = MathUtil.clamp(minZoom, zoom, maxZoom);
        final double actualZoomValue = Math.floor(requestedZoom * 16.0) / 16.0;
        this.zoom = actualZoomValue;
        world.updateZoom(this.zoom);
        return actualZoomValue;
    }

    @Override
    public Vector moveWithinVisualBounds(final Vector delta, final Bounds bounds) {
        final var legalPostionArea = Bounds.atPosition(bounds.position(),
                Math.max(1, bounds.width() - world.visibleArea().width()),
                Math.max(1, bounds.height() - world.visibleArea().height()));

        final double movementX = MathUtil.clamp(
                legalPostionArea.minX() - position().x(),
                delta.x(),
                legalPostionArea.maxX() - position().x());

        final double movementY = MathUtil.clamp(
                legalPostionArea.minY() - position().y(),
                delta.y(),
                legalPostionArea.maxY() - position().y());

        move($(movementX, movementY));
        return position;
    }

    @Override
    public double updateZoomRelative(final double delta) {
        return updateZoom(requestedZoom + delta);
    }

    @Override
    public Camera addShake(CameraShake shake) {
        start = Time.now();
        activeShake = shake;
        return this;
    }

    @Override
    public double zoom() {
        return world.cameraZoom();
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

        if (activeShake != null) {
            shake = activeShake.getDistorion(start, now);
            if (activeShake.isFinished(start, now)) {
                activeShake = null;
            }
        } else {
            shake = Vector.zero();
        }
    }
}
