package dev.screwbox.core.graphics.options;

//TODO test

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.utils.Validate;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, ShadowOptions) backdrop occluders}.
 *
 * @param backdropDistance distance between occluder and backdrop
 * @param isRounded        shadow will be rounded (makes rendering slower)
 * @param isDarkenOccluder configures if occluder body itself is affected by shadow
 * @since 3.23.0
 */
public record ShadowOptions(double backdropDistance, boolean isRounded, boolean isDarkenOccluder) {

    public ShadowOptions {
        Validate.positive(backdropDistance, "backdrop distance must be positive");
    }

    /**
     * Creates a new instance with rounded shadow.
     */
    public static ShadowOptions rounded() {
        return new ShadowOptions(true);
    }

    /**
     * Creates a new instance with angular shadow.
     */
    public static ShadowOptions angular() {
        return new ShadowOptions(false);
    }

    private ShadowOptions(final boolean isRounded) {
        this(1, isRounded, false);
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public ShadowOptions backdropDistance(final double backdropDistance) {
        return new ShadowOptions(backdropDistance, isRounded, isDarkenOccluder);
    }


    /**
     * Occluder itself will be affected by shadow.
     */
    public ShadowOptions affectOccluder() {
        return new ShadowOptions(backdropDistance, isRounded, true);
    }
}
