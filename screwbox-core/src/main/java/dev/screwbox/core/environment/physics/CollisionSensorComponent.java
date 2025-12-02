package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    /**
     * All {@link Entity entities} collided with parent {@link Entity}. Will be automatically populated by {@link CollisionSensorSystem}.
     */
    public transient List<Entity> collidedEntities = new ArrayList<>();

    /**
     * Distance from sensor {@link Entity} used to detect colliding {@link Entity entities}.
     *
     * @since 3.1.0
     */
    public double range = 0.001;

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collidedEntities = (List<Entity>) in.readObject();
    }

    @Serial
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new ArrayList<>(collidedEntities));
    }
}
