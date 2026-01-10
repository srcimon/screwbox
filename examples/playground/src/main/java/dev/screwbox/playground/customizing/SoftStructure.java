package dev.screwbox.playground.customizing;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.playground.ClothComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SoftStructure {

    public static List<Entity> createBox(final Bounds bounds, final Size cellCount, final IdPool idPool) {
        //TODO implement
        return new ArrayList<>();
    }

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

    static abstract class EntityStructure implements SoftStructureEntities {
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


    static class ClothEntitiesImpl extends EntityStructure implements ClothEntities {

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
