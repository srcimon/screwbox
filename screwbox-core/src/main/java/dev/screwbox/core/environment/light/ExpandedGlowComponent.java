package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.core.graphics.Light;

import java.io.Serial;

/**
 * Adds an expanded glow at the {@link Entity#bounds()}.
 *
 * @since 3.9.0
 */
public class ExpandedGlowComponent implements Component {

    //TODO changelog
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
     */
    public LensFlare lensFlare;

    /**
     * Creates a new instance specifying radius and {@link Color}.
     */
    public ExpandedGlowComponent(final double radius, final Color color) {
        this.radius = radius;
        this.color = color;
    }
}