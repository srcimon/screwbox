package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/**
 * Adds collisions with other soft body entities containing this component.
 *
 * @since 3.17.0
 */
public class SoftBodyCollisionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The number of the collided {@link Polygon#definitionNotes()} of the {@link SoftBodyComponent#shape}.
     *
     * @since 3.18.0
     */
    public transient Set<Integer> collidedNodes = new HashSet<>();

    /**
     * The number of the collided {@link Polygon#segments()} of the {@link SoftBodyComponent#shape}.
     *
     * @since 3.18.0
     */
    public transient Set<Integer> collidedSegments = new HashSet<>();

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collidedNodes = (Set<Integer>) in.readObject();
        collidedSegments = (Set<Integer>) in.readObject();
    }

    @Serial
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(collidedNodes);
        out.writeObject(collidedSegments);
    }
}
