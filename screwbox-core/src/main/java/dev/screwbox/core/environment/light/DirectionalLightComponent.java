package dev.screwbox.core.environment.light;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

//TODO add to guide

/**
 * Adds a directional light at the top border of the entity.
 *
 * @since 3.22.0
 */
public class DirectionalLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * {@link Color} of the directional light.
     */
    public Color color = Color.BLACK;

    /**
     * {@link Angle} of the light source.
     */
    public Angle angle = Angle.none();
}