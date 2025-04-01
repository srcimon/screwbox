package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FluidInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double modifier;

    public FluidInteractionComponent() {
        this(4);
    }

    public FluidInteractionComponent(final  double modifier) {
        this.modifier = modifier;
    }
}
