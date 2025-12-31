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

    /**
     * Primary {@link Color} of the fluid. Will be used to fill the fluid polygon.
     */
    public final Color color;

    /**
     * Secondary optional {@link Color} of the fluid. Will be used to fill the fluid polygon using a gradient.
     */
    public final Color secondaryColor;

    /**
     * Surface {@link Color} of the fluid. Will be used to draw a line on the surface using {@link #surfaceStrokeWidth}.
     *
     * @since 3.20.0
     */
    public Color surfaceColor = null;

    /**
     * Stroke width used to draw a line on the surface.
     *
     * @since 3.20.0
     */
    public int surfaceStrokeWidth = 2;
    /**
     * Draw order used for rendering.
     *
     * @since 3.16.0
     */
    public int drawOrder;

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
