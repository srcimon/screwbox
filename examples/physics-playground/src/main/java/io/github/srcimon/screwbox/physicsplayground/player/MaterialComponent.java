package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

public class MaterialComponent implements Component {

    private final Material material;

    public MaterialComponent(Material material) {
        this.material = material;
    }
}
