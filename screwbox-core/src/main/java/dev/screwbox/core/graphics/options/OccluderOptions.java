package dev.screwbox.core.graphics.options;

//TODO test

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.utils.Validate;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, OccluderOptions) backdrop occluders}.
 *
 * @param backdropDistance distance between occluder and backdrop
 * @param isRounded        shadow will be rounded (makes rendering slower)
 * @param isFloating       configure if shadow will float on the backdrop or be connected to the occluder (connected will make rendering slower)
 * @since 3.23.0
 */
public record OccluderOptions(double backdropDistance, boolean isRounded, boolean isFloating) {

    public OccluderOptions {
        Validate.positive(backdropDistance, "backdrop distance must be positive");
    }

    /**
     * Creates a new instance with floating shadow.
     */
    public static OccluderOptions floating() {
        return new OccluderOptions(true);
    }

    /**
     * Creates a new instance with connected shadow (Is much slower than {@link #floating()}.).
     */
    public static OccluderOptions connected() {
        return new OccluderOptions(false);
    }

    private OccluderOptions(final boolean isLoose) {
        this(1, false, isLoose);
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public OccluderOptions backdropDistance(final double backdropDistance) {
        return new OccluderOptions(backdropDistance, isRounded, isFloating);
    }

    /**
     * Sets rendering to rounded (Reduces rendering speed.).
     */
    public OccluderOptions roundend() {
        return new OccluderOptions(backdropDistance, true, isFloating);
    }
}
