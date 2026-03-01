package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Duration;

//TODO document
//TODO test
public record ShockwaveOptions(double radius, int intensity, Duration duration) {

    public static ShockwaveOptions radius(final double radius) {
        return new ShockwaveOptions(radius, 30, Duration.oneSecond());
    }

    public ShockwaveOptions intensity(final int intensity) {
        return new ShockwaveOptions(radius, intensity, duration);
    }

    public ShockwaveOptions duration(final Duration duration) {
        return new ShockwaveOptions(radius, intensity, duration);
    }
}
