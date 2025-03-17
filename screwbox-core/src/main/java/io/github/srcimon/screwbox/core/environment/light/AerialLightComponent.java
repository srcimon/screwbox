package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;

import java.io.Serial;

/**
 * Adds an area to the {@link World} that is fully or partially illuminated.
 *
 * @see io.github.srcimon.screwbox.core.graphics.Light#addAerialLight(Bounds, Color)
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
