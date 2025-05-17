package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

/**
 * Will render fluid when added to entity also containing {@link FluidComponent}.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 2.19.0
 */
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
