package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

public class PlayerInfoComponent implements Component {

    public Material currentGroundMaterial = Material.UNKNOWN;
}
