package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

public class ClothComponent implements Component {

    public Entity[][] mesh;

    public ClothComponent(Entity[][] nodes) {
        this.mesh = nodes;
    }
}
