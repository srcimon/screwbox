package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Size;

public class ClothComponent implements Component {

    public Entity[][] mesh;
    public Size normalSize;

    public ClothComponent(Entity[][] nodes, Size normalSize) {
        this.mesh = nodes;
        this.normalSize = normalSize;
    }
}
