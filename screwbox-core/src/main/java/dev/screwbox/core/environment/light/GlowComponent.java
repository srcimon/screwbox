package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.Light;

import java.io.Serial;

/**
 * Adds a glow at the entity position.
 */
public class GlowComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Specify the radius of the glow effect.
     */
    public double radius;

    /**
     * Specify the {@link Color} of the glow effect.
     */
    public Color color;

    /**
     * Specify the {@link LensFlare} caused by the glow effect. Will use {@link Light#defaultLensFlare()} when
     * left empty.
     *
     * @since 3.8.0
     */
    public LensFlare lensFlare;

    /**
     * Creates a new instance specifying radius and {@link Color}.
     */
    public GlowComponent(final double radius, final Color color) {
        this.radius = radius;
        this.color = color;
    }
}
