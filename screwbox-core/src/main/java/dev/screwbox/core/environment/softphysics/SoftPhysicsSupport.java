package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.List;

//TODO document
//TODO add to guide
//TODO changelog

/**
 * Adds support for common tasks to work with soft bodies.
 *
 * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
 * @since 3.18.0
 */
public class SoftPhysicsSupport {

    private SoftPhysicsSupport() {

    }

    /**
     * Returns the {@link Polygon} of the specified {@link SoftBodyComponent}.
     *
     */
    public static Polygon toPolygon(final SoftBodyComponent softBody) {
        final List<Vector> nodes = new ArrayList<>();
        for (final var node : softBody.nodes) {
            nodes.add(node.position());
        }
        return Polygon.ofNodes(nodes);
    }
}
