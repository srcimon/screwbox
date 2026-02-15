package dev.screwbox.core.graphics.options;

//TODO test

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Light;
import dev.screwbox.core.utils.Validate;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, OccluderOptions) backdrop occluders}.
 *
 * @param backdropDistance   distance between occluder and backdrop
 * @param isRounded          shadow will be rounded (makes rendering slower)
 * @param isAffectedByShadow configures if occluder body itself is affected by shadow
 * @since 3.23.0
 */
public record OccluderOptions(double backdropDistance, boolean isRounded, boolean isAffectedByShadow) {

    public OccluderOptions {
        Validate.positive(backdropDistance, "backdrop distance must be positive");
    }

    /**
     * Creates a new instance with rounded shadow.
     */
    public static OccluderOptions rounded() {
        return new OccluderOptions(true);
    }

    /**
     * Creates a new instance with angular shadow.
     */
    public static OccluderOptions angular() {
        return new OccluderOptions(false);
    }

    private OccluderOptions(final boolean isRounded) {
        this(1, isRounded, false);
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public OccluderOptions backdropDistance(final double backdropDistance) {
        return new OccluderOptions(backdropDistance, isRounded, isAffectedByShadow);
    }


    /**
     * Occluder itself will be affected by shadow.
     */
    public OccluderOptions affectOccluder() {
        return new OccluderOptions(backdropDistance, isRounded, true);
    }
}
