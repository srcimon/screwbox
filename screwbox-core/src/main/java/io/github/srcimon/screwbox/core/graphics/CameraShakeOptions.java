package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;

public record CameraShakeOptions(Duration duration, double strength, Duration interval) {

    public CameraShakeOptions {
        if (strength <= 0) {
            throw new IllegalArgumentException("strength must be positive");
        }
    }

    private CameraShakeOptions(final Duration duration) {
       this(duration, 10, Duration.ofMillis(50));
    }

    public static CameraShakeOptions infinite() {
        return new CameraShakeOptions(Duration.none());
    }

    public static CameraShakeOptions lastingForDuration(final Duration duration) {
        return new CameraShakeOptions(duration);
    }

    public CameraShakeOptions strength(final double strength) {
      return new CameraShakeOptions(duration, strength, interval);
    }

    public CameraShakeOptions interval(final Duration interval) {
        return new CameraShakeOptions(duration, strength, interval);
    }
}
