package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds more information on collisions when added to {@link Entity} also having a {@link CollisionSensorComponent}.
 *
 * @see CollisionDetailsSystem
 * @see CollisionSensorComponent
 * @since 2.10.0
 */
public class CollisionDetailsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Entity entityLeft;
    public Entity entityTop;
    public Entity entityRight;
    public Entity entityBottom;

    public boolean touchesLeft;
    public boolean touchesTop;
    public boolean touchesRight;
    public boolean touchesBottom;

    public Time lastBottomContact = Time.unset();
}
