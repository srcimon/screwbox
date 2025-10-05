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
 * @see Light#addAreaLight(Bounds, Color, double, boolean)
 * @since 2.18.0
 */
public class AreaLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * {@link Color} of the area light. {@link Color#BLACK} is the default values.
     */
    public Color color;

    /**
     * Will create rounded edges.
     *
     * @since 3.10.0
     */
    public double curveRadius;

    /**
     * Will create an additional fading effect around the light area using {@link #curveRadius}.
     *
     * @since 3.10.0
     */
    public boolean isFadeout;

    /**
     * Creates a new instance using max brightness.
     */
    public AreaLightComponent() {
        this(Color.BLACK);
    }

    /**
     * Creates a new instance using the specified color.
     */
    public AreaLightComponent(final Color color) {
        this.color = color;
    }

}
