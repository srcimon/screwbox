package dev.screwbox.core.graphics.options;

//TODO test

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.utils.Validate;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, BackdropShadowOptions) backdrop occluders}.
 *
 * @param backdropDistance   distance between occluder and backdrop
 * @param isRounded          shadow will be rounded (makes rendering slower)
 * @param isFloating         configure if shadow will float on the backdrop or be connected to the occluder (connected will make rendering slower)
 * @param isAffectedByShadow configures if occluder body itself is affected by shadow
 * @since 3.23.0
 */
public record BackdropShadowOptions(double backdropDistance, boolean isRounded, boolean isFloating,
                                    boolean isAffectedByShadow) {

    public BackdropShadowOptions {
        Validate.positive(backdropDistance, "backdrop distance must be positive");
    }

    /**
     * Creates a new instance with floating shadow.
     */
    public static BackdropShadowOptions floating() {
        return new BackdropShadowOptions(true, false);
    }

    /**
     * Creates a new instance with connected shadow (Is much slower than {@link #floating()}.).
     */
    public static BackdropShadowOptions connected() {
        return new BackdropShadowOptions(false, false);
    }

    private BackdropShadowOptions(final boolean isLoose, final boolean isAffectedByShadow) {
        this(1, false, isLoose, isAffectedByShadow);
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public BackdropShadowOptions backdropDistance(final double backdropDistance) {
        return new BackdropShadowOptions(backdropDistance, isRounded, isFloating, isAffectedByShadow);
    }

    /**
     * Sets rendering to rounded (Reduces rendering speed.).
     */
    public BackdropShadowOptions roundend() {
        return new BackdropShadowOptions(backdropDistance, true, isFloating, isAffectedByShadow);
    }

    /**
     * Occluder itself will be affected by shadow.
     */
    public BackdropShadowOptions affectedByShadow() {
        return new BackdropShadowOptions(backdropDistance, isRounded, isFloating, true);
    }
}
