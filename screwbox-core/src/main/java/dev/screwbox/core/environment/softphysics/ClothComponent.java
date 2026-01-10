package dev.screwbox.core.environment.softphysics;

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
}
