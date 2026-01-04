package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

public class ClothComponent implements Component {

    public Entity[][] nodes;

    public ClothComponent(Entity[][] nodes) {
        this.nodes = nodes;
    }
}
