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

    public static List<Entity> createCloth(final Bounds bounds, final Size cellCount, final IdPool idPool) {
        List<Entity> cloth = new ArrayList<>();
        Map<Offset, Entity> clothMap = new HashMap<>();


        for (var offset : cellCount.all()) {
            final Vector position = bounds.origin().add(offset.x() * bounds.width() / cellCount.width(), offset.y() * bounds.height() / cellCount.height());
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent());
            clothMap.put(offset, node);
            cloth.add(node);

        }

        var outline = cellCount.outline();
        for (int index = 0; index < outline.size(); index++) {
            int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            var targetId = clothMap.get(outline.get(nextIndex)).forceId();
            clothMap.get(outline.get(index)).add(new SoftLinkComponent(targetId));
        }

        for (int y = 0; y < cellCount.height() - 1; y++) {
            for (int x = 0; x < cellCount.width() - 1; x++) {
                var index = Offset.at(x, y);
                var rightIndex = Offset.at(x + 1, y);
                var bottomIndex = Offset.at(x, y + 1);
                boolean connectRight = !(cellCount.isOutline(index) && cellCount.isOutline(rightIndex));
                boolean connectBottom = !(cellCount.isOutline(index) && cellCount.isOutline(bottomIndex));
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


        Entity[][] mesh = new Entity[cellCount.width()][cellCount.height()];
        for (var offset : cellCount.all()) {
            mesh[offset.x()][offset.y()] = clothMap.get(offset);
        }
        cloth.getFirst().add(new SoftBodyComponent());
        cloth.getFirst().add(new ClothComponent(mesh, Size.of(bounds.width() / cellCount.width(), bounds.height() / cellCount.height())));
        SoftPhysicsSupport.updateLinkLengths(cloth);
        return cloth;
    }
}
