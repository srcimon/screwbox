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
 * @see Light#addAerialLight(Bounds, Color)
 * @since 2.18.0
 */
public class AerialLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Color color;

    /**
     * Creates a new instance using max brightness.
     */
    public AerialLightComponent() {
        this(Color.BLACK);
    }

    /**
     * Creates a new instance using the specified color.
     */
    public AerialLightComponent(final Color color) {
        this.color = color;
    }

}
