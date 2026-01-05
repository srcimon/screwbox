package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Size;

public class ClothComponent implements Component {

    public Entity[][] nodes;
    public Size normalSize;

    public ClothComponent(final Entity[][] nodes, Size normalSize) {
        this.nodes = nodes;
        this.normalSize = normalSize;
    }



    //TODO define this component first. How to auto populate component using an entity system interpreting links and structures?
    //NOTES:
    //U need a single clothrendercomponent to fix rendering of cloth nodes in same order.
    //  splitting to ClothNodeComponent(peerA.position, peerB.position) seems nice but would not alloe ordered rendering
}
