package dev.screwbox.core.graphics.options;

//TODO test

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.utils.Validate;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, ShadowOptions) backdrop occluders}.
 *
 * @param backdropDistance   distance between occluder and backdrop
 * @param isRounded          shadow will be rounded (makes rendering slower)
 * @param isFloating         configure if shadow will float on the backdrop or be connected to the occluder (connected will make rendering slower)
 * @param isOccluderAffected configures if occluder body itself is affected by shadow
 * @since 3.23.0
 */
public record ShadowOptions(double backdropDistance, boolean isRounded, boolean isFloating,
                            boolean isOccluderAffected) {

    public ShadowOptions {
        Validate.positive(backdropDistance, "backdrop distance must be positive");
    }

    /**
     * Creates a new instance with floating shadow.
     */
    public static ShadowOptions floating() {
        return new ShadowOptions(false, false);
    }

    /**
     * Creates a new instance with connected shadow (Is much slower than {@link #floating()}.).
     */
    public static ShadowOptions connected() {
        return new ShadowOptions(false, false);
    }

    private ShadowOptions(final boolean isLoose, final boolean isAffectedByShadow) {
        this(1, false, isLoose, isAffectedByShadow);
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public ShadowOptions backdropDistance(final double backdropDistance) {
        return new ShadowOptions(backdropDistance, isRounded, isFloating, isOccluderAffected);
    }

    /**
     * Sets rendering to rounded (Reduces rendering speed.).
     */
    public ShadowOptions roundend() {
        return new ShadowOptions(backdropDistance, true, isFloating, isOccluderAffected);
    }

    /**
     * Occluder itself will be affected by shadow.
     */
    public ShadowOptions affectOccluder() {
        return new ShadowOptions(backdropDistance, isRounded, isFloating, true);
    }
}
