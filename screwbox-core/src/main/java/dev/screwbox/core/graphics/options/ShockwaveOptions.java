package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.utils.Validate;

/**
 * Specify options used to for triggering shockwaves in {@link dev.screwbox.core.graphics.PostProcessing}.
 *
 * @param radius    radius of shockwave when finished
 * @param intensity intensity of shockwave reflection
 * @param duration  duration that shockwave needs to expand
 * @param width     initial width of the shockwave
 * @param growth    growth rate that is applied on width while shockwave expands
 * @see dev.screwbox.core.graphics.PostProcessing#triggerShockwave(Vector, ShockwaveOptions)
 * @since 3.24.0
 */
public record ShockwaveOptions(double radius, int intensity, Duration duration, double width, Percent growth) {

    public ShockwaveOptions {
        Validate.range(radius, 2, 4096, "radius must be in range 2 to 4096");
        Validate.positive(intensity, "intensity must be positive");
        Validate.positive(width, "width must be positive");
    }

    /**
     * Create options for the specified radius.
     */
    public static ShockwaveOptions radius(final double radius) {
        return new ShockwaveOptions(radius, 30, Duration.oneSecond(), 10, Percent.of(0.1));
    }

    /**
     * Returns a new instance with the specified intensity.
     */
    public ShockwaveOptions intensity(final int intensity) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    /**
     * Returns a new instance with the specified duration.
     */
    public ShockwaveOptions duration(final Duration duration) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    /**
     * Returns a new instance with the specified width.
     */
    public ShockwaveOptions width(final double width) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    /**
     * Returns a new instance with the specified growth.
     */
    public ShockwaveOptions growth(final Percent growth) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }
}
