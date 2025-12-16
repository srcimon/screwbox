package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;

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

    //TODO fix serialization
    /**
     * The number of the collided {@link Polygon#definitionNotes()} of the {@link SoftBodyComponent#shape}.
     */
    public Set<Integer> collidedNodes = new HashSet<>();

    /**
     * The number of the collided {@link Polygon#segments()} of the {@link SoftBodyComponent#shape}.
     */
    public Set<Integer> collidedSegments = new HashSet<>();
}
