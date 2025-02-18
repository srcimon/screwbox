package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;

import java.io.Serial;

/**
 * Enforce ground contact for jumping. Automatically disable (and re-enable) {@link JumpControlComponent} based on the
 * last {@link CollisionDetailsComponent#lastBottomContact last bottom contact}.
 * Also needs {@link CollisionDetailsComponent} to work properly.
 *
 * @since 2.15.0
 */
public class SuspendJumpControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Grace period that is granted after last bottom contact to allow jumping.
     */
    public Duration gracePeriod = Duration.ofMillis(200);

    /**
     * Time of the last detected jump.
     */
    public Time lastJumpDetection = Time.now();//TODO make unset

    /**
     * Time of the last detected ground contact.
     */
    public Time lastGroundDetection = Time.now();//TODO make unset

    /**
     * Maximum number of jumps in a row.
     */
    public int maxJumps = 1;

    /**
     * Number of jumps remaining.
     */
    public int remainingJumps = maxJumps;
}
