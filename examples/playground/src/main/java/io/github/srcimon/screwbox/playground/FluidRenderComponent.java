package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Color;

import java.io.Serial;

public class FluidRenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Color color;

    public FluidRenderComponent(final Color color) {
        this.color = color;
    }
}
