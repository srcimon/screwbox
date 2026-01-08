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

    public static List<Entity> createBox(final Bounds bounds, final Size cellCount, final IdPool idPool) {
        //TODO implement
        return new ArrayList<>();
    }

    public static List<Entity> createCloth(final Bounds bounds, final Size cellCount, final IdPool idPool) {
        Map<Offset, Entity> clothMap1 = new HashMap<>();
        for (var offset1 : cellCount.all()) {
            final Vector position = bounds.origin().add(offset1.x() * bounds.width() / cellCount.width(), offset1.y() * bounds.height() / cellCount.height());
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent());
            clothMap1.put(offset1, node);
        }

        var outline = cellCount.outline();
        for (int index = 0; index < outline.size(); index++) {
            int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            var targetId = clothMap1.get(outline.get(nextIndex)).forceId();
            clothMap1.get(outline.get(index)).add(new SoftLinkComponent(targetId));
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
                    targetIds.add(clothMap1.get(rightIndex).forceId());
                }
                if (connectBottom) {
                    targetIds.add(clothMap1.get(bottomIndex).forceId());
                }
                if (!targetIds.isEmpty()) {
                    clothMap1.get(index).add(new SoftStructureComponent(targetIds));
                }
            }
        }
        Map<Offset, Entity> clothMap = clothMap1;

        Entity[][] mesh = new Entity[cellCount.width()][cellCount.height()];
        for (final var offset : cellCount.all()) {
            mesh[offset.x()][offset.y()] = clothMap.get(offset);
        }
        List<Entity> cloth = new ArrayList<>();
        for (final var offset : cellCount.all()) {
            cloth.add(clothMap.get(offset));
        }
        cloth.getFirst().add(new SoftBodyComponent());
        cloth.getFirst().add(new ClothComponent(mesh, Size.of(bounds.width() / cellCount.width(), bounds.height() / cellCount.height())));
        SoftPhysicsSupport.updateLinkLengths(cloth);
        return cloth;
    }

}
