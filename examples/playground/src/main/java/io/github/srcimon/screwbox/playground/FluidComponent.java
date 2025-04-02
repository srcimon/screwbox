package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FluidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Fluid fluid;

    public FluidComponent(final int nodeCount) {
        this.fluid = new Fluid(nodeCount);
    }

}
