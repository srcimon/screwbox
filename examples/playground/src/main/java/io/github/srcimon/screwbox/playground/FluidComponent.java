package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FluidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final FluidSurface surface;

    public FluidComponent(int nodeCount) {
        this.surface = new FluidSurface(nodeCount);
    }
}
