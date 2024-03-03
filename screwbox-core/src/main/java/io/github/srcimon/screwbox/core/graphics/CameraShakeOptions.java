package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;

public class CameraShakeOptions {

    private final Duration duration;
    private double strength = 10;
    private Duration interval = Duration.ofMillis(50);

    private CameraShakeOptions(final Duration duration) {
        this.duration = duration;
    }

    public static CameraShakeOptions infinite() {
        return new CameraShakeOptions(Duration.none());
    }

    public static CameraShakeOptions lastingForDuration(final Duration duration) {
        return new CameraShakeOptions(duration);
    }

    public CameraShakeOptions strength(final double strength) {
        if (strength <= 0) {
            throw new IllegalArgumentException("strength must be positive");
        }
        this.strength = strength;
        return this;
    }

    public CameraShakeOptions interval(final Duration interval) {
        this.interval = interval;
        return this;
    }

    public double strength() {
        return strength;
    }

    public Duration duration() {
        return duration;
    }

    public Duration interval() {
        return interval;
    }
}
