package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothPrototype {

    public static List<Entity> createCloth(final Bounds bounds, final Size cellSize, final IdPool idPool) {
        List<Entity> cloth = new ArrayList<>();
        Map<Offset, Entity> clothMap = new HashMap<>();
        Entity[][] mesh = new Entity[cellSize.height()][cellSize.width()];

        for (var offset : cellSize.all()) {
            final Vector position = bounds.origin().add(offset.x() * bounds.width() / cellSize.width(), offset.y() * bounds.height() / cellSize.height());
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent());
            clothMap.put(offset, node);
            cloth.add(node);
            mesh[offset.y()][offset.x()] = node;
        }
        var outline = cellSize.outline();
        for (int index = 0; index < outline.size(); index++) {
            int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            var targetId = clothMap.get(outline.get(nextIndex)).forceId();
            clothMap.get(outline.get(index)).add(new SoftLinkComponent(targetId));
        }

        for (int y = 0; y < cellSize.height() - 1; y++) {
            for (int x = 0; x < cellSize.width() - 1; x++) {
                var index = Offset.at(x, y);
                var rightIndex = Offset.at(x + 1, y);
                var bottomIndex = Offset.at(x, y + 1);
                boolean connectRight = !(cellSize.isOutline(index) && cellSize.isOutline(rightIndex));
                boolean connectBottom = !(cellSize.isOutline(index) && cellSize.isOutline(bottomIndex));
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


        cloth.getFirst().add(new SoftBodyComponent());
        cloth.getFirst().add(new ClothComponent(mesh, Size.of(bounds.width() / cellSize.width(), bounds.height() / cellSize.height())));
        SoftPhysicsSupport.updateLinkLengths(cloth);
        return cloth;
    }
}
