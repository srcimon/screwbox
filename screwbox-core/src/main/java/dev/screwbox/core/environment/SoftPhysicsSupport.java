package dev.screwbox.core.environment;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Helps creating soft bodies and ropes.
 *
 * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
 * @since 3.20.0
 */
public final class SoftPhysicsSupport {

    private SoftPhysicsSupport() {
    }

    /**
     * Create a rope between the to specified positions. This only creates the basic entities linked by {@link SoftLinkComponent}.
     * The created {@link Entity entities} can and should be customized afterwards to create a usefully rope.
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
        updateSoftLinks(rope);
        return rope;
    }

    /**
     * Creates a soft body using the specified {@link Polygon}. The soft body will not have any stabilizing {@link SoftStructureComponent} or {@link SoftBodyShapeComponent}.
     * Use {@link #createStabilizedSoftBody(Polygon, IdPool)} to automatically add stabilizing {@link SoftStructureComponent}.
     *
     * @param outline polygon with at least two nodes specifying the soft body outline
     * @param idPool  id pool used to allocate entity ids
     */
    public static List<Entity> createSoftBody(final Polygon outline, final IdPool idPool) {
        Objects.requireNonNull(outline, "polygon must not be null");
        Objects.requireNonNull(idPool, "idPool must not be null");
        Validate.range(outline.nodeCount(), 2, 4096, "polygon must have between 2 and 4096 nodes");
        final List<Entity> softBody = new ArrayList<>();

        for (int nodeNr = 0; nodeNr < outline.nodeCount(); nodeNr++) {
            final int id = idPool.allocateId();
            final int targetId = nodeNr == outline.nodeCount() - 1
                ? softBody.getFirst().forceId()
                : idPool.peekId();

            softBody.add(new Entity(id)
                .bounds(Bounds.atPosition(outline.node(nodeNr), 1, 1))
                .add(new PhysicsComponent())
                .add(new SoftLinkComponent(targetId)));
        }

        softBody.getFirst().add(new SoftBodyComponent());
        updateSoftLinks(softBody);
        return softBody;
    }

    /**
     * Creates a soft body using the specified {@link Polygon}. The soft body will have stabilizing {@link SoftStructureComponent}.
     * Use {@link #createSoftBody(Polygon, IdPool)} if no stabilizing is wanted.
     *
     * @param outline polygon with at least two nodes specifying the soft body outline
     * @param idPool  id pool used to allocate entity ids
     */
    public static List<Entity> createStabilizedSoftBody(final Polygon outline, final IdPool idPool) {
        final List<Entity> softBody = createSoftBody(outline, idPool);
        final Polygon closedOutline = outline.close(); // has to be closed to find opposing index
        for (int i = 0; i < closedOutline.nodeCount(); ++i) {
            var opposingIndex = closedOutline.opposingIndex(i);
            if (opposingIndex.isPresent()) {
                final var entity = softBody.get(i);
                final var opposingEntity = softBody.get(opposingIndex.get());
                final var opposingStructure = opposingEntity.get(SoftStructureComponent.class);
                if (isNull(opposingStructure) || opposingStructure.targetIds[0] != entity.forceId()) {
                    entity.add(new SoftStructureComponent(opposingEntity.forceId()));
                }
            }
        }
        updateSoftStructures(softBody);
        return softBody;
    }

    /**
     * Updates the {@link SoftLinkComponent#length} and {@link SoftStructureComponent#lengths} properties for all entities specified.
     * Requires all target entities also to be contained within the list.
     */
    public static void updateLinkLengths(final List<Entity> entities) {
        updateSoftLinks(entities);
        updateSoftStructures(entities);
    }

    private static void updateSoftLinks(final List<Entity> entities) {
        for (final var entity : entities) {
            final var link = entity.get(SoftLinkComponent.class);
            if (nonNull(link)) {
                link.length = detectLength(entity.position(), link.targetId, entities);
            }
        }
    }

    private static void updateSoftStructures(final List<Entity> entities) {
        for (final var entity : entities) {
            final var structure = entity.get(SoftStructureComponent.class);
            if (nonNull(structure)) {
                for (int i = 0; i < structure.targetIds.length; i++) {
                    structure.lengths[i] = detectLength(entity.position(), structure.targetIds[i], entities);
                }
            }
        }
    }

    private static double detectLength(final Vector startPosition, final int targetId, final List<Entity> entities) {
        for (final var entity : entities) {
            if (entity.id().isPresent() && entity.forceId() == targetId) {
                return entity.position().distanceTo(startPosition);
            }
        }
        throw new IllegalArgumentException("missing target entity with id " + targetId);
    }

}
