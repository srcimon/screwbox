package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
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

    public Set<Integer> intrudedNodes = new HashSet<>();
    public Set<Integer> intrudedSegments = new HashSet<>();
}
