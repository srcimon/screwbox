package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Color;

import java.io.Serial;

public class FluidRenderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Color color;
    public final Color secondaryColor;

    public FluidRenderComponent() {
        this(Color.hex("#777fd8").opacity(0.5), Color.hex("#3445ff").opacity(0.5));
    }
    public FluidRenderComponent(final Color color) {
        this(color, null);
    }

    public FluidRenderComponent(final Color color, final Color secondaryColor) {
        this.color = color;
        this.secondaryColor = secondaryColor;
    }
}
