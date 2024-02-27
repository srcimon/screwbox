package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.utils.Lurk;

import java.io.Serial;

/**
 * Adds chaotic movement to an {@link Entity}. Therefore updates the {@link PhysicsComponent#momentum} of the {@link Entity}.
 */
public class ChaoticMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double speed;
    public final Lurk xModifier;
    public final Lurk yModifier;

    public ChaoticMovementComponent(final double speed, final Duration interval) {
        this.speed = speed;
        xModifier = Lurk.intervalWithDeviation(interval, Percent.half());
        yModifier = Lurk.intervalWithDeviation(interval, Percent.half());
    }
}
