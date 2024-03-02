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

    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private Lurk y = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private double shakeStrength = 0;
    private Vector shake = Vector.zero();
    Vector position = Vector.zero();
    private double zoom = 2;
    private double wantedZoom = zoom;
    private double minZoom = 2;
    private double maxZoom = 5;

    //TODO ADD ZOOM TO shake
    public void updatePosition(Vector position) {
        this.position = position;
    }

    @Override
    public void update() {
        Time time = Time.now();
        shake = $(x.value(time), y.value(time)).multiply(shakeStrength);
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
        return actualZoomValue;
    }
}
