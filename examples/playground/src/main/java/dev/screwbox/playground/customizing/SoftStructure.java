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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Entity> cloth = new ArrayList<>();
        for (final var offset : workCellCount.all()) {
            cloth.add(clothMap.get(offset));
        }
        cloth.getFirst().add(new SoftBodyComponent());
        cloth.getFirst().add(new ClothComponent(mesh, Size.of(bounds.width() / workCellCount.width(), bounds.height() / workCellCount.height())));
        SoftPhysicsSupport.updateLinkLengths(cloth);
        List<Entity> outlineTop = new ArrayList<>();
        for (int index = 0; index < workCellCount.width(); index++) {
            outlineTop.add(clothMap.get(Offset.at(index, 0)));
        }
        List<Entity> outlineBottom = new ArrayList<>();
        for (int index = 0; index < workCellCount.width(); index++) {
            outlineBottom.add(clothMap.get(Offset.at(index, workCellCount.height() - 1)));
        }
        return new ClothEntitiesImpl(cloth, outlineTop, outlineBottom);
    }

    record ClothEntitiesImpl(List<Entity> entities, List<Entity> outlineTopEntities,
                             List<Entity> outlineButtomntities) implements ClothEntities {

        @Override
        public Entity root() {
            return entities.getFirst();
        }

        @Override
        public List<Entity> outlineTop() {
            return outlineTopEntities;
        }

        @Override
        public List<Entity> outlineBottom() {
            return outlineButtomntities;
        }
    }

}
