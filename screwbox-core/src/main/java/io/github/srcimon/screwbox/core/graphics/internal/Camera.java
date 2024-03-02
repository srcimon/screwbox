package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Lurk;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import static io.github.srcimon.screwbox.core.Vector.$;

public class Camera implements Updatable {

    private final DefaultWorld world;
    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private Lurk y = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private double shakeStrength = 0;
    private Vector shake = Vector.zero();
    Vector position = Vector.zero();
    private double zoom = 2;
    private double wantedZoom = zoom;
    private double minZoom = 2;
    private double maxZoom = 5;
    private Time start = null;
    private Time end = null;

    public Camera(DefaultWorld world) {
        this.world = world;
    }

    public void addTimedShake(double strength, Duration interval, Duration duration) {
        start = Time.now();
        end = start.plus(duration);
        shakeStrength = strength;
        x = Lurk.intervalWithDeviation(interval, Percent.half());
        x = Lurk.intervalWithDeviation(interval, Percent.half());

    }//TODO CameraShakeOptions...

    //TODO ADD ZOOM TO shake
    public void updatePosition(Vector position) {
        this.position = position;
        world.updateCameraPosition(focus());
    }

    public Vector position() {
        return position;
    }

    public Vector focus() {
        return position.add(shake);
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

    public double updateZoom(final double zoom) {
        this.wantedZoom = MathUtil.clamp(minZoom, zoom, maxZoom);
        final double actualZoomValue = Math.floor(wantedZoom * 16.0) / 16.0;
        this.zoom = actualZoomValue;
        world.updateZoom(this.zoom);
        return actualZoomValue;
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
