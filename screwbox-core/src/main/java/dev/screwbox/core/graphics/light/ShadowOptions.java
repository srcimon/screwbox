package dev.screwbox.core.graphics.light;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.utils.Validate;

import java.io.Serializable;

/**
 * Configures the shadows created by {@link Light#addBackgdropOccluder(Bounds, ShadowOptions) backdrop occluders}.
 *
 * @param backdropDistance distance between occluder and backdrop
 * @param isRounded        shadow will be rounded (makes rendering slower)
 * @param isAffectOccluder configures if occluder body itself is affected by shadow
 * @param distortion       configures the distortion of the shadow
 * @param offset           offset between shadow casting object and shadow
 * @since 3.23.0
 */
public record ShadowOptions(double backdropDistance, boolean isRounded, boolean isAffectOccluder,
                            Percent distortion, Vector offset) implements Serializable {

    private static final ShadowOptions ROUNDED = new ShadowOptions(true);
    private static final ShadowOptions ANGULAR = new ShadowOptions(false);

    public ShadowOptions {
        Validate.zeroOrPositive(backdropDistance, "backdrop distance must be positive");
        Validate.isTrue(!offset.isZero() || backdropDistance > 0, "backdrop distance can only be zero when offset is set");
    }

    /**
     * Creates a new instance with rounded shadow.
     */
    public static ShadowOptions rounded() {
        return ROUNDED;
    }

    /**
     * Creates a new instance with angular shadow.
     */
    public static ShadowOptions angular() {
        return ANGULAR;
    }

    private ShadowOptions(final boolean isRounded) {
        this(1, isRounded, false, Percent.zero(), Vector.zero());
    }

    /**
     * Sets distance to between occluder and backdrop.
     */
    public ShadowOptions backdropDistance(final double backdropDistance) {
        return new ShadowOptions(backdropDistance, isRounded, isAffectOccluder, distortion, offset);
    }

    /**
     * Occluder itself will be affected by shadow.
     */
    public ShadowOptions affectOccluder() {
        return new ShadowOptions(backdropDistance, isRounded, true, distortion, offset);
    }

    /**
     * Sets percentage of distortion of shadow. Will create a stretched shadow when value above zero is set.
     */
    public ShadowOptions distortion(final Percent distortion) {
        return new ShadowOptions(backdropDistance, isRounded, isAffectOccluder, distortion, offset);
    }

    /**
     * Sets the offset between shadow casting object and shadow.
     */
    public ShadowOptions offset(final Vector offset) {
        return new ShadowOptions(backdropDistance, isRounded, isAffectOccluder, distortion, offset);
    }
}
