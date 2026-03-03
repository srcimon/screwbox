package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;

//TODO document
//TODO test
public record ShockwaveOptions(double radius, int intensity, Duration duration, double width, Percent growth) {

    public static ShockwaveOptions radius(final double radius) {
        return new ShockwaveOptions(radius, 30, Duration.oneSecond(), 30, Percent.of(0.2));
    }

    public ShockwaveOptions intensity(final int intensity) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    public ShockwaveOptions duration(final Duration duration) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    public ShockwaveOptions width(final double width) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }

    public ShockwaveOptions growth(final Percent growth) {
        return new ShockwaveOptions(radius, intensity, duration, width, growth);
    }
}
