package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    //TODO implement createBox(final Bounds bounds, final Size cellCount, final IdPool idPool) {

    public static ClothEntities createCloth(final Bounds bounds, final Size cellCount, final IdPool idPool) {
        var workCellCount = cellCount.expand(1);
        Map<Offset, Entity> clothMap = new HashMap<>();
        for (var offset1 : workCellCount.all()) {
            final Vector position = bounds.origin().add(offset1.x() * bounds.width() / workCellCount.width(), offset1.y() * bounds.height() / workCellCount.height());
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent());
            clothMap.put(offset1, node);
        }

        var outline = workCellCount.outline();
        for (int index = 0; index < outline.size(); index++) {
            int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            var targetId = clothMap.get(outline.get(nextIndex)).forceId();
            clothMap.get(outline.get(index)).add(new SoftLinkComponent(targetId));
        }

        for (int y = 0; y < workCellCount.height() - 1; y++) {
            for (int x = 0; x < workCellCount.width() - 1; x++) {
                var index = Offset.at(x, y);
                var rightIndex = Offset.at(x + 1, y);
                var bottomIndex = Offset.at(x, y + 1);
                boolean connectRight = !(workCellCount.isOutline(index) && workCellCount.isOutline(rightIndex));
                boolean connectBottom = !(workCellCount.isOutline(index) && workCellCount.isOutline(bottomIndex));
                List<Integer> targetIds = new ArrayList<>();
                if (connectRight) {
                    targetIds.add(clothMap.get(rightIndex).forceId());
                }
                if (connectBottom) {
                    targetIds.add(clothMap.get(bottomIndex).forceId());
                }
                if (!targetIds.isEmpty()) {
                    clothMap.get(index).add(new SoftStructureComponent(targetIds));
                }
            }
        }

        Entity[][] mesh = new Entity[workCellCount.width()][workCellCount.height()];
        for (final var offset : workCellCount.all()) {
            mesh[offset.x()][offset.y()] = clothMap.get(offset);
        }
        ClothEntitiesImpl structure = new ClothEntitiesImpl();
        for (final var offset : workCellCount.all()) {
            structure.add(clothMap.get(offset));
        }
        SoftPhysicsSupport.updateLinkLengths(structure.all());

        structure.root()
            .add(new SoftBodyComponent())
            .add(new ClothComponent(mesh, Size.of(bounds.width() / workCellCount.width(), bounds.height() / workCellCount.height())));

        for (int index = 0; index < workCellCount.width(); index++) {
            structure.tag(clothMap.get(Offset.at(index, 0)), "outline-top");
        }
        for (int index = 0; index < workCellCount.width(); index++) {
            structure.tag(clothMap.get(Offset.at(index, workCellCount.height() - 1)), "outline-bottom");
        }
        for(var offset : workCellCount.outline()) {
            structure.tag(clothMap.get(offset), "outline");
        }

        return structure;
    }

    static abstract class EntityStructure implements SoftPhysicsEntities {
        private final Map<Entity, Set<String>> taggedEntities = new HashMap<>();
        private final List<Entity> entities = new ArrayList<>();

        protected void add(Entity entity) {
            taggedEntities.put(entity, new HashSet<>());
            this.entities.add(entity);
        }

        protected void tag(Entity entity, String tag) {
            taggedEntities.get(entity).add(tag);
        }

        protected List<Entity> entitiesWithTag(String tag) {
            return taggedEntities.entrySet().stream().filter(e -> taggedEntities.get(e.getKey()).contains(tag)).map(Map.Entry::getKey).toList();
        }

        public Entity root() {
            return entities.getFirst();
        }

        public List<Entity> all() {
            return Collections.unmodifiableList(entities);
        }
    }


    private static class ClothEntitiesImpl extends EntityStructure implements ClothEntities {

        @Override
        public List<Entity> outline() {
            return entitiesWithTag("outline");
        }

        @Override
        public List<Entity> outlineTop() {
            return entitiesWithTag("outline-top");
        }

        @Override
        public List<Entity> outlineBottom() {
            return entitiesWithTag("outline-bottom");
        }
    }

}
