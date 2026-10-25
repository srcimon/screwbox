package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to mark the start of a rope. Will create a fast accessible list of {@link Entity entities} linked from this
 * {@link Entity} with {@link SoftLinkComponent}.
 *
 * @since 3.16.0
 */
public class RopeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Shape of the rope. Will be updated by {@link RopeSystem}.
     *
     * @since 3.18.0
     */
    public Polygon shape;

    /**
     * List of {@link Entity entities} that are part of the rope. Will be updated by {@link RopeSystem}.
     */
    public transient List<Entity> nodes = new ArrayList<>();

    /**
     * Keeps all segments of the rope at exactly the specified distance. Requires all connected nodes to have a {@link PhysicsComponent}.
     *
     * @since 3.39.0
     */
    public boolean isLengthFixed = false;

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        nodes = (List<Entity>) in.readObject();
    }

    @Serial
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(new ArrayList<>(nodes));
    }
}
