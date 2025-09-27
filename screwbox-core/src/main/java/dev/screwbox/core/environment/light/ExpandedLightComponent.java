package dev.screwbox.core.environment.light;

import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;

import java.io.Serial;

/**
 * Adds an area to the {@link World} that is fully or partially illuminated.
 *
 * @see Light#addExpandedLight(Bounds, Color, double, double)
 * @since 2.18.0
 */
public class ExpandedLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * {@link Color} of the expanded light. {@link Color#BLACK} is the default values.
     */
    public Color color;

    /**
     * Will create rounded edges.
     *
     * @since 3.10.0
     */
    public double curveRadius;

    /**
     * Will create an additional fading effect around the light area.
     *
     * @since 3.10.0
     */
    public double fade;

    /**
     * Creates a new instance using max brightness.
     */
    public ExpandedLightComponent() {
        this(Color.BLACK);
    }

    /**
     * Creates a new instance using the specified color.
     */
    public ExpandedLightComponent(final Color color) {
        this.color = color;
    }

}
