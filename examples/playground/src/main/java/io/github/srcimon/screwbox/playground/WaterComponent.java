package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class WaterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final WaterSurface waterSurface;

    public WaterComponent(int nodeCount) {
        this.waterSurface = new WaterSurface(nodeCount);
    }
}
