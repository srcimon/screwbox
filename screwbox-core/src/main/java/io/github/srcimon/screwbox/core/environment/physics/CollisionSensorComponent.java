package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects all collided {@link Entity entities}.
 *
 * @see CollisionSensorSystem
 * @see CollisionDetailsComponent
 */
public class CollisionSensorComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public List<Entity> collidedEntities = new ArrayList<>();

}
