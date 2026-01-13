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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toSet;

/**
 * Helps with the creation of complex multi {@link Entity} structures like soft bodies and ropes.
 *
 * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
 * @since 3.20.0
 */
public final class SoftPhysicsSupport {

    private SoftPhysicsSupport() {
    }

    /**
     * Easy access to {@link Entity entities} of a rope created by {@link SoftPhysicsSupport}.
     *
     * @since 3.20.0
     */
    public static class RopeEntities extends ArrayList<Entity> {

        /**
         * The root {@link Entity} contains the {@link RopeComponent}.
         */
        public Entity root() {
            return getFirst();
        }

        /**
         * The {@link Entity} in the middle of the rope.
         */
        public Entity center() {
            return get(size() / 2 - 1);
        }

        /**
         * All {@link Entity entities} between {@link #root()} and {@link #end()}.
         */
        public List<Entity> connectors() {
            return subList(1, size() - 1);
        }

        /**
         * The {@link Entity} marking the end of the rope.
         */
        public Entity end() {
            return getLast();
        }
    }

    /**
     * Create a {@link RopeEntities rope} between the to specified positions. This only creates the basic entities linked by {@link SoftLinkComponent}.
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
     * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
     */
    public static RopeEntities createRope(final Vector start, final Vector end, final int nodeCount, final IdPool idPool) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        Objects.requireNonNull(idPool, "idPool must not be null");
        Validate.range(nodeCount, 3, 4096, "nodeCount must be between 3 and 4096");
        Validate.notEqual(start, end, "rope start should be different from end");

        final var rope = new RopeEntities();
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
     * Easy access to {@link Entity entities} of a soft body created by {@link SoftPhysicsSupport}.
     *
     * @since 3.20.0
     */
    public static class SoftBodyEntities extends ArrayList<Entity> {

        /**
         * The root {@link Entity} contains the {@link SoftBodyComponent}.
         */
        public Entity root() {
            return getFirst();
        }

        /**
         * All {@link Entity entities} that have a {@link SoftStructureComponent} to stabilize the soft body.
         */
        public List<Entity> supportOrigins() {
            return stream()
                .filter(entity -> entity.hasComponent(SoftStructureComponent.class))
                .toList();
        }

        /**
         * All {@link Entity entities} that are targeted by a {@link #supportOrigins()} to stabilize the soft body.
         */
        public List<Entity> supportTargets() {
            final var targetIds = stream()
                .map(entity -> entity.get(SoftStructureComponent.class))
                .filter(Objects::nonNull)
                .flatMapToInt(structure -> Arrays.stream(structure.targetIds))
                .boxed()
                .collect(toSet());

            return stream()
                .filter(entity -> targetIds.contains(entity.forceId()))
                .toList();
        }
    }

    /**
     * Creates a soft body using the specified {@link Polygon}. The soft body will not have any stabilizing {@link SoftStructureComponent} or {@link SoftBodyShapeComponent}.
     * Use {@link #createStabilizedSoftBody(Polygon, IdPool)} to automatically add stabilizing {@link SoftStructureComponent}.
     *
     * @param outline polygon with at least two nodes specifying the soft body outline
     * @param idPool  id pool used to allocate entity ids
     * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
     */
    public static SoftBodyEntities createSoftBody(final Polygon outline, final IdPool idPool) {
        Objects.requireNonNull(outline, "polygon must not be null");
        Objects.requireNonNull(idPool, "idPool must not be null");
        Validate.range(outline.nodeCount(), 2, 4096, "polygon must have between 2 and 4096 nodes");
        SoftBodyEntities softBody = new SoftBodyEntities();

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
     * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
     */
    public static SoftBodyEntities createStabilizedSoftBody(final Polygon outline, final IdPool idPool) {
        final SoftBodyEntities softBody = createSoftBody(outline, idPool);
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


    /**
     * Creates a soft body cloth with the specified mesh size.
     *
     * @param meshSize size of the cloth mesh
     * @param idPool   id pool used to allocate entity ids
     * @see <a href="https://screwbox.dev/docs/guides/soft-physics/">Documentation</a>
     * @since 3.20.0
     */
    public static ClothEntities createCloth(final Bounds bounds, final Size meshSize, final IdPool idPool) {
        final var fullSize = meshSize.expand(1);
        final Map<Offset, Entity> clothMap = new HashMap<>();
        for (var offset : fullSize.all()) {
            final Vector position = bounds.origin().add(offset.x() * bounds.width() / fullSize.width(), offset.y() * bounds.height() / fullSize.height());
            clothMap.put(offset, new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent()));
        }

        linkClothOutline(fullSize, clothMap);
        linkClothMesh(fullSize, clothMap);
        ClothEntities structure = new ClothEntities();
        for (final var offset : fullSize.all()) {
            structure.add(clothMap.get(offset));
        }

        final Entity[][] mesh = createMesh(fullSize, clothMap);
        structure.getFirst()
            .add(new SoftBodyComponent())
            .add(new ClothComponent(mesh, Size.of(bounds.width() / fullSize.width(), bounds.height() / fullSize.height())));

        addClothTags(fullSize, clothMap);

        SoftPhysicsSupport.updateLinkLengths(structure);
        return structure;
    }

    private static void addClothTags(final Size fullSize, final Map<Offset, Entity> clothMap) {
        for (int index = 0; index < fullSize.width(); index++) {
            clothMap.get(Offset.at(index, 0)).tag("cloth-top-border");
        }
        for (int index = 0; index < fullSize.width(); index++) {
            clothMap.get(Offset.at(index, fullSize.height() - 1)).tag("cloth-bottom-border");
        }
        for (int index = 0; index < fullSize.height(); index++) {
            clothMap.get(Offset.at(0, index)).tag("cloth-left-border");
        }
        for (int index = 0; index < fullSize.width(); index++) {
            clothMap.get(Offset.at(fullSize.width() - 1, index)).tag("cloth-right-border");
        }
        for (var offset : fullSize.outline()) {
            clothMap.get(offset).tag("cloth-outline");
        }
        clothMap.get(Offset.at(0, 0)).tag("cloth-top-left-edge");
        clothMap.get(Offset.at(0, fullSize.height() - 1)).tag("cloth-bottom-left-edge");
        clothMap.get(Offset.at(fullSize.width() - 1, fullSize.height() - 1)).tag("cloth-bottom-right-edge");
        clothMap.get(Offset.at(fullSize.width() - 1, 0)).tag("cloth-top-right-edge");
    }

    private static Entity[][] createMesh(Size fullSize, Map<Offset, Entity> clothMap) {
        final Entity[][] mesh = new Entity[fullSize.width()][fullSize.height()];
        for (final var offset : fullSize.all()) {
            mesh[offset.x()][offset.y()] = clothMap.get(offset);
        }
        return mesh;
    }

    private static void linkClothMesh(final Size fullSize, final Map<Offset, Entity> clothMap) {
        for (int y = 0; y < fullSize.height() - 1; y++) {
            for (int x = 0; x < fullSize.width() - 1; x++) {
                var index = Offset.at(x, y);
                var rightIndex = Offset.at(x + 1, y);
                var bottomIndex = Offset.at(x, y + 1);
                final List<Integer> targetIds = new ArrayList<>();
                if (!(fullSize.isOutline(index) && fullSize.isOutline(rightIndex))) {
                    targetIds.add(clothMap.get(rightIndex).forceId());
                }
                if (!(fullSize.isOutline(index) && fullSize.isOutline(bottomIndex))) {
                    targetIds.add(clothMap.get(bottomIndex).forceId());
                }
                if (!targetIds.isEmpty()) {
                    clothMap.get(index).add(new SoftStructureComponent(targetIds));
                }
            }
        }
    }

    private static void linkClothOutline(final Size fullSize, final Map<Offset, Entity> clothMap) {
        final var outline = fullSize.outline();
        for (int index = 0; index < outline.size(); index++) {
            final int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            final var targetId = clothMap.get(outline.get(nextIndex)).forceId();
            clothMap.get(outline.get(index)).add(new SoftLinkComponent(targetId));
        }
    }

    /**
     * Easy access to {@link Entity entities} of a soft body cloth created by {@link SoftPhysicsSupport}.
     *
     * @since 3.20.0
     */
    public static class ClothEntities extends ArrayList<Entity> {

        /**
         * All edge {@link Entity entities}.
         */
        public List<Entity> edges() {
            return List.of(topLeftEdge(), topRightEdge(), bottomRightEdge(), bottomLeftEdge());
        }

        /**
         * The top left edge {@link Entity}.
         */
        public Entity topLeftEdge() {
            return singleTaggedBy("cloth-top-left-edge");
        }

        /**
         * The top right edge {@link Entity}.
         */
        public Entity topRightEdge() {
            return singleTaggedBy("cloth-top-right-edge");
        }

        /**
         * The bottom left edge {@link Entity}.
         */
        public Entity bottomLeftEdge() {
            return singleTaggedBy("cloth-bottom-left-edge");
        }

        /**
         * The bottom right edge {@link Entity}.
         */
        public Entity bottomRightEdge() {
            return singleTaggedBy("cloth-bottom-right-edge");
        }

        /**
         * The root {@link Entity} containing the {@link ClothComponent}.
         */
        public Entity root() {
            return getFirst();
        }

        /**
         * All {@link Entity entities} that belong to the cloth outline.
         */
        public List<Entity> outline() {
            return taggedBy("cloth-outline");
        }

        /**
         * All {@link Entity entities} that belong to the cloth top border.
         */
        public List<Entity> topBorder() {
            return taggedBy("cloth-top-border");
        }

        /**
         * All {@link Entity entities} that belong to the cloth bottom border.
         */
        public List<Entity> bottomBorder() {
            return taggedBy("cloth-bottom-border");
        }

        /**
         * All {@link Entity entities} that belong to the cloth left border.
         */
        public List<Entity> rightBorder() {
            return taggedBy("cloth-right-border");
        }

        /**
         * All {@link Entity entities} that belong to the cloth right border.
         */
        public List<Entity> leftBorder() {
            return taggedBy("cloth-left-border");
        }

        /**
         * All {@link Entity entities} within the mesh that do not belong to the cloth bottom outline.
         */
        public List<Entity> meshNodes() {
            return notTaggedBy("cloth-outline");
        }

        private List<Entity> notTaggedBy(final String tag) {
            return stream().filter(entity -> !entity.hasTag(tag)).toList();
        }

        private Entity singleTaggedBy(final String tag) {
            return stream().filter(entity -> entity.hasTag(tag)).findFirst().orElseThrow();
        }

        private List<Entity> taggedBy(final String tag) {
            return stream().filter(entity -> entity.hasTag(tag)).toList();
        }
    }
}
