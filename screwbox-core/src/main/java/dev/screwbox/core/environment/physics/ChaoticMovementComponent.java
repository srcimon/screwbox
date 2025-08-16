package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.Noise;

import java.io.Serial;

/**
 * Adds chaotic movement to an {@link Entity}. Therefore updates the {@link PhysicsComponent#velocity} of the {@link Entity}.
 */
public class ChaoticMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double speed;
    public final Noise xModifier;
    public final Noise yModifier;
    public final Duration interval;
    public Vector baseSpeed;

    public ChaoticMovementComponent(final double speed, final Duration interval) {
        this(speed, interval, Vector.zero());
    }

    public ChaoticMovementComponent(final double speed, final Duration interval, final Vector baseSpeed) {
        this.speed = speed;
        xModifier = Noise.variableInterval(interval);
        yModifier = Noise.variableInterval(interval);
        this.interval = interval;
        this.baseSpeed = baseSpeed;
    }
}
