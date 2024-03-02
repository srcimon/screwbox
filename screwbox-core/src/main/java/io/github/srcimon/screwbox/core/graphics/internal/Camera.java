package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Lurk;

import static io.github.srcimon.screwbox.core.Vector.$;

public class Camera implements Updatable {

    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private Lurk y = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private double shakeStrength = 10;
    private Vector shake = Vector.zero();
    Vector position = Vector.zero();

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
}
