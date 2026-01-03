package dev.screwbox.core.environment;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Helps creating soft bodies and ropes.
 *
 * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
 * @since 3.20.0
 */
//TODO actually add docs in guide
public final class SoftPhysicsSupport {

    private record Link(int start, int end) {

        public static Link create(int a, int b) {
            return a > b ? new Link(a, b) : new Link(b, a);
        }
    }

    private SoftPhysicsSupport() {
    }

    /**
     * Create a rope between the to specified positions. This only creates the basic entities linked by {@link SoftLinkComponent}.
     * The created {@link Entity entities} can and should be customized afterwards to create a usefull rope.
     * <p>
     * The first {@link Entity} in the list will be the one containing
     * the {@link RopeComponent}. So if you want to actually see the rope, add a {@link RopeRenderComponent} to the
     * first {@link Entity} in the list.
     *
     * @param start     star position
     * @param end       end position
     * @param nodeCount number of nodes of the rope (between 3 and 4096)
     * @param idPool    id pool used to allocate entity ids
     */
    public static List<Entity> createRope(final Vector start, final Vector end, final int nodeCount, final IdPool idPool) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        Objects.requireNonNull(idPool, "idPool must not be null");
        Validate.range(nodeCount, 3, 4096, "nodeCount must be between 3 and 4096");
        Validate.notEqual(start, end, "rope start should be different from end");

        final List<Entity> rope = new ArrayList<>();
        for (int nodeNr = nodeCount - 1; nodeNr >= 0; nodeNr--) {
            final Vector nodePosition = end.add(start.substract(end).multiply((double) nodeNr / (nodeCount - 1)));
            rope.add(new Entity(idPool.allocateId())
                .bounds(Bounds.atPosition(nodePosition, 1, 1))
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(idPool.peekId())));
        }
        rope.getFirst().add(new RopeComponent());
        rope.getLast().remove(SoftLinkComponent.class);
        return rope;
    }

    /**
     * Create a soft body using the specified positions
     */
    public static List<Entity> createSoftBody(final Polygon polygon, final IdPool idPool) {
        Objects.requireNonNull(polygon, "polygon must not be null");
        Objects.requireNonNull(idPool, "idPool must not be null");
        Validate.range(polygon.nodeCount(), 2, 4096, "polygon must have between 2 and 4096 nodes");
        Polygon workPolygon = polygon.close();//TODO really needed?
        final List<Entity> entities = new ArrayList<>();

        workPolygon.nodes().stream()
            .map(position -> new Entity(idPool.allocateId()).bounds(Bounds.atPosition(position, 1, 1)).add(new PhysicsComponent()))
            .forEach(entities::add);

        entities.getFirst().add(new SoftBodyComponent());
        for (int i = 0; i < workPolygon.nodes().size(); i++) {
            int grabIndex = i + 1;
            if (grabIndex >= workPolygon.nodes().size()) {
                grabIndex = 0;
            }
            entities.get(i).add(new SoftLinkComponent(entities.get(grabIndex).forceId()));
        }
        //TODO handle last position = first position
        return entities;
    }

    public static List<Entity> createStabilizedSoftBody(final Polygon polygon, final IdPool idPool) {
        Polygon workPolygon = polygon.close();
        List<Entity> entities = createSoftBody(polygon, idPool);

        final Set<Link> links = new HashSet<>();
        for (int i = 0; i < workPolygon.nodeCount(); ++i) {
            var opposingIndex = workPolygon.opposingIndex(i);
            if (opposingIndex.isPresent()) {
                links.add(Link.create(i, opposingIndex.get()));
            }
        }
        var distinctStarts = links.stream().map(l -> l.start).distinct().toList();
        for (var start : distinctStarts) {
            var targets = fetchTargets(start, links);
            for (int x = 0; x < entities.size(); x++) {
                if (x == start) {
                    entities.get(x).add(new SoftStructureComponent(targets.stream().map(i -> entities.get(i).forceId()).toList()));
                }
            }
        }
        return entities;
    }

    private static List<Integer> fetchTargets(int start, Set<Link> links) {
        List<Integer> targets = new ArrayList<>();
        for (var link : links) {
            if (link.start == start) {
                targets.add(link.end);
            }
        }
        return targets;
    }

    //TODO create cloth
}
