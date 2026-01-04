package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothPrototype {

    //TODO workaround snapping origin
    public static List<Entity> createCloth(Bounds bounds, int size, IdPool idPool) {
        List<Entity> cloth = new ArrayList<>();
        Grid clothGrid = new Grid(bounds, size);
        Map<Offset, Entity> clothMap = new HashMap<>();
        for (final var clothNode : clothGrid.nodes()) {
            Entity node = new Entity(idPool.allocateId())
                .bounds(Bounds.atOrigin(clothGrid.nodeBounds(clothNode).origin(), 1, 1))
                .add(new PhysicsComponent());
            cloth.add(node);
            clothMap.put(clothNode, node);
        }

        for(final Offset clothNode : clothGrid.nodes()) {
            List<Integer> ids = new ArrayList<>();
            List<Offset> offsets = clothGrid.adjacentNodes(clothNode);
            for(final var adjacent : offsets) {
                ids.add(clothMap.get(adjacent).forceId());
            }
            SoftStructureComponent component = new SoftStructureComponent(ids);

            //TODO replace with
            SoftPhysicsSupport.updateLinkLengths(cloth);

            for(int i = 0; i < ids.size(); ++i) {
                component.lengths[i] = 16;//TODO not considering height
            }
            clothMap.get(clothNode).add(component);//TODO duplicate links

        }
        return cloth;
    }
}
