package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    public transient List<Entity> collidedEntities = new ArrayList<>();

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collidedEntities = new ArrayList<>();
    }
}
