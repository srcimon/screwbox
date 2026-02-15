package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.options.ShadowOptions;

import java.io.Serial;

//TODO test
//TODO document within soft body guide
/**
 * Adds a backdrop shadow to a rope. Also requires {@link Entity} to have a {@link RopeComponent} and a {@link RopeRenderComponent}.
 *
 * @since 3.23.0
 */
public class RopeOccluderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Updates the stroke width of the resulting shadow. Higher values can reduce flickering of thin rope shadows,
     * but can also reduce overall rendering quality.
     */
    public double strokeWidthModifier = 2.0;

    /**
     * Configure shadows created by the rope.
     */
    public ShadowOptions options;

    /**
     * Creates a new instance using the specified {@link ShadowOptions}.
     */
    public RopeOccluderComponent(final ShadowOptions options) {
        this.options = options;
    }
}
