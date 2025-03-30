package io.github.srcimon.screwbox.core.graphics.options;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Configures the shake of the {@link Camera}. Can be applied by {@link Camera#shake(CameraShakeOptions)}.
 *
 * @param duration  the {@link Duration} of the shake
 * @param xStrength the x-strength of the shake
 * @param yStrength the y-strength of the shake
 * @param interval  the {@link Duration} between direction changes (may very to make it more realistic)
 * @param swing     the maximum {@link Rotation} applied to the {@link Screen}. When using multiple {@link Camera cameras}.
 *                  The sum of all swings is applied on the {@link Screen}.
 * @param ease      the {@link Ease} used to calculate the strength of the shake at a specific time
 */
public record CameraShakeOptions(Duration duration, double xStrength, double yStrength, Duration interval,
                                 Rotation swing, Ease ease) {

    public CameraShakeOptions {
        Validate.zeroOrPositive(xStrength, "strength must be positive");
        Validate.zeroOrPositive(yStrength, "strength must be positive");
    }

    private CameraShakeOptions(final Duration duration) {
        this(duration, 0, 0, Duration.ofMillis(50), Rotation.none(), Ease.LINEAR_OUT);
    }

    /**
     * The {@link Camera} will only stop shaking when invoking {@link Camera#stopShaking()} or applying another shake.
     */
    public static CameraShakeOptions infinite() {
        return new CameraShakeOptions(Duration.none());
    }

    /**
     * The {@link Camera} will only stop shaking after the given {@link Duration}.
     */
    public static CameraShakeOptions lastingForDuration(final Duration duration) {
        return new CameraShakeOptions(duration);
    }

    /**
     * Sets the x-strength of the shake.
     */
    public CameraShakeOptions xStrength(final double xStrength) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, swing, ease);
    }

    /**
     * Sets the y-strength of the shake.
     */
    public CameraShakeOptions yStrength(final double yStrength) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, swing, ease);
    }

    /**
     * Sets the x- and the y-strength of the shake. Default: 10.
     */
    public CameraShakeOptions strength(final double strength) {
        return new CameraShakeOptions(duration, strength, strength, interval, swing, ease);
    }

    /**
     * Sets the {@link Duration} between direction changes. Default 50s.
     */
    public CameraShakeOptions interval(final Duration interval) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, swing, ease);
    }

    /**
     * Sets the maximum {@link Screen#shake()} applied. {@link Screen#shake()} comes with quite a fps drop.
     */
    public CameraShakeOptions swing(final Rotation swing) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, swing, ease);
    }

    /**
     * Sets the {@link Ease} used to calculate the strength of the shake at a specific time.
     */
    public CameraShakeOptions ease(final Ease ease) {
        return new CameraShakeOptions(duration, xStrength, yStrength, interval, swing, ease);
    }
}
