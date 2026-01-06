package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothPrototype {

    //TODO workaround snapping origin
    public static List<Entity> createCloth(Bounds bounds, Size size, IdPool idPool) {
        List<Entity> cloth = new ArrayList<>();
        Map<Offset, Entity> clothMap = new HashMap<>();

        for (var offset : size.allOffsets()) {
            final Vector position = bounds.origin().add(offset.x() * size.width(), offset.y() * size.height());
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(position, 1, 1))
                .add(new PhysicsComponent());
            clothMap.put(offset, node);
            cloth.add(node);

        }
        var outline = size.outlineOffsets();
        for (int index = 0; index < outline.size(); index++) {
            int nextIndex = index + 1 == outline.size() ? 0 : index + 1;
            var targetId = clothMap.get(outline.get(nextIndex)).forceId();;
            clothMap.get(outline.get(index)).add(new SoftLinkComponent(targetId));
        }
//        Grid clothGrid = new Grid(bounds, size);
//        Map<Offset, Entity> clothMap = new HashMap<>();
//        for (final var clothNode : clothGrid.nodes()) {
//            Entity node = new Entity(idPool.allocateId())
//                .bounds(Bounds.atOrigin(clothGrid.nodeBounds(clothNode).origin(), 1, 1))
//                .add(new PhysicsComponent());
//            cloth.add(node);
//            clothMap.put(clothNode, node);
//        }
//
//        for (final Offset clothNode : clothGrid.nodes()) {
//            List<Integer> ids = new ArrayList<>();
//            List<Offset> offsets = clothGrid.adjacentNodes(clothNode);
//            for (final var adjacent : offsets) {
//                ids.add(clothMap.get(adjacent).forceId());
//            }
//
//            SoftStructureComponent component = new SoftStructureComponent(ids);
//
//
//            clothMap.get(clothNode).add(component);//TODO duplicate links
//        }
//
//        Entity[][] nodes = new Entity[clothGrid.width()][clothGrid.height()];
//        for (final Offset clothNode : clothGrid.nodes()) {
//            nodes[clothNode.x()][clothNode.y()] = clothMap.get(clothNode);
//        }
//
//
//

        cloth.getFirst().add(new SoftBodyComponent());
        System.out.println(cloth.getFirst());
        SoftPhysicsSupport.updateLinkLengths(cloth);
        return cloth;
    }
}
