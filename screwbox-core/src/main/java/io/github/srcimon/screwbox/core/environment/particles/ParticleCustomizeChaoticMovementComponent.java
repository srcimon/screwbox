package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleCustomizeChaoticMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public Duration interval;
    public Vector baseSpeed;

    public ParticleCustomizeChaoticMovementComponent(final double speed, final Duration interval, final Vector baseSpeed) {
        this.speed = speed;
        this.interval = interval;
        this.baseSpeed = baseSpeed;
    }
}
