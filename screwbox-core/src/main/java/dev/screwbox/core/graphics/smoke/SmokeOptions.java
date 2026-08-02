package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.smoke.styles.TrueColorSmokeStyle;
import dev.screwbox.core.utils.Validate;

import static java.util.Objects.requireNonNull;

/**
 * Customize smoke style and behavior.
 *
 * @param viscosity        viscosity of the smoke, default is 0.0000000004
 * @param diffusion        diffusion rate of the smoke, default is 0.000001
 * @param iterations       number of iterations used to calculate behavior. Has heavy performance impact
 * @param opacity          opacity of the smoke
 * @param fade             speed of the smoke dissapearing
 * @param velocity         base velocity of the smoke simulation. velocity of whole simulation will adapt when non null
 * @param velocityAdaption speed of the adaption to the default velocity of the smoke simulation
 * @param style            rendering style for drawing the smoke
 */
public record SmokeOptions(Percent viscosity, Percent diffusion, int iterations, Percent opacity, double fade,
                           Vector velocity, Percent velocityAdaption, SmokeStyle style) {


    /**
     * Smoke that does not fade over time. Will still diffuse into the environment and out of the screen even without
     * any {@link #fade()}.
     */
    public static SmokeOptions noFade() {
        return new SmokeOptions(0);
    }

    /**
     * Smoke that slowly fades away.
     */
    public static SmokeOptions slowFade() {
        return new SmokeOptions(0.04);
    }

    public SmokeOptions {
        requireNonNull(viscosity, "viscosity must not be null");
        requireNonNull(diffusion, "diffusion must not be null");
        requireNonNull(opacity, "opacity must not be null");
        requireNonNull(velocityAdaption, "velocity adaption must not be null");
        requireNonNull(style, "style must not be null");
        Validate.range(fade, 0, 10, "fade must be between 0 and 10");
        Validate.range(iterations, 1, 10, "iterations must be between 0 and 10");
    }

    private SmokeOptions(final double fade) {
        this(Percent.of(0.0000000004), Percent.of(0.000001), 2, Percent.max(), fade, null, Percent.of(0.001), new TrueColorSmokeStyle());
    }

    /**
     * Set the diffusion rate of the smoke.
     */
    public SmokeOptions diffusion(final Percent diffusion) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Set the viscosity of the smoke.
     */
    public SmokeOptions viscosity(final Percent viscosity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Set the fade of the smoke.
     */
    public SmokeOptions fade(final double fade) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Set the opacity of the smoke rendering.
     */
    public SmokeOptions opacity(final Percent opacity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Set the number of iterations used to calculate behavior. Has heavy performance impact.
     */
    public SmokeOptions iterations(final int iterations) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Sets the base velocity of the smoke.
     */
    public SmokeOptions velocity(final Vector velocity) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Sets the adaption rate to the base velocity of the smoke.
     */
    public SmokeOptions velocityAdaption(final Percent velocityAdaption) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }

    /**
     * Sets the rendering stlye of the smoke.
     */
    public SmokeOptions style(final SmokeStyle style) {
        return new SmokeOptions(viscosity, diffusion, iterations, opacity, fade, velocity, velocityAdaption, style);
    }
}
