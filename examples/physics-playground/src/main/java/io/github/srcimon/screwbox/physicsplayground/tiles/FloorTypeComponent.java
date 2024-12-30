package io.github.srcimon.screwbox.physicsplayground.tiles;

import io.github.srcimon.screwbox.core.environment.Component;

public class FloorTypeComponent implements Component {

    public FloorType type;

    public FloorTypeComponent(FloorType type) {
        this.type = type;
    }
}
