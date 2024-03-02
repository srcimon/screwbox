package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Lurk;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import static io.github.srcimon.screwbox.core.Vector.$;

public class DefaultCamera implements Camera, Updatable {

    private final DefaultWorld world;
    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private Lurk y = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private double shakeStrength = 0;
    private Vector shake = Vector.zero();
    Vector position = Vector.zero();
    private double zoom = 2;
    private double requestedZoom = zoom;
    private double minZoom = 2;
    private double maxZoom = 5;
    private Time start = null;
    private Time end = null;

    public DefaultCamera(DefaultWorld world) {
        this.world = world;
    }

    //TODO CameraShakeOptions...

    //TODO ADD ZOOM TO shake
    @Override
    public Camera updatePosition(Vector position) {
        this.position = position;
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

        updatePosition(position.add(movementX, movementY));
        return position();
    }

    @Override
    public double updateZoomRelative(final double delta) {
        return updateZoom(requestedZoom + delta);
    }

    @Override
    public Camera addShake(double strength, Duration interval, Duration duration) {
        start = Time.now();
        end = start.plus(duration);
        shakeStrength = strength;
        x = Lurk.intervalWithDeviation(interval, Percent.half());
        y = Lurk.intervalWithDeviation(interval, Percent.half());

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

        if (start != null) {
            Duration elapsed = Duration.between(start, now);
            var progress = Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
            shake = $(x.value(now), y.value(now)).multiply(shakeStrength * progress.invert().value());
            if (progress.isMax()) {
                start = null;
                end = null;
            }
        } else {
            shake = Vector.zero();
        }
    }
}
