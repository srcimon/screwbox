package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.utils.Noise;

import java.io.Serial;

/**
 * Adds chaotic movement to an {@link Entity}. Therefore updates the {@link PhysicsComponent#momentum} of the {@link Entity}.
 */
public class ChaoticMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double speed;
    public Vector baseSpeed;
    public final Noise xModifier;
    public final Noise yModifier;

    public ChaoticMovementComponent(final double speed, final Duration interval) {
        this(speed, interval, Vector.zero());
    }

    public ChaoticMovementComponent(final double speed, final Duration interval, final Vector baseSpeed) {
        this.speed = speed;
        xModifier = Noise.variableInterval(interval);
        yModifier = Noise.variableInterval(interval);
        this.baseSpeed = baseSpeed;
    }
}
