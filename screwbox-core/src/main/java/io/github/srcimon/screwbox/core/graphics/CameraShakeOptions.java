package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;

public class CameraShakeOptions {

    private final Duration duration;
    private double strength;
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

}
