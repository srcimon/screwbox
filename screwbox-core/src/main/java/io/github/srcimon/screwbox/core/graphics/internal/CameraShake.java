package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.utils.Lurk;

public class CameraShake {

    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(100), Percent.half());
    private Lurk y= Lurk.intervalWithDeviation(Duration.ofMillis(100), Percent.half());
    private double strength = 4;

    public Vector applyShake(Vector position) {
        Time now = Time.now();
        return position.add(
                x.value(now) * strength,
                y.value(now) * strength
        );
    }
}
