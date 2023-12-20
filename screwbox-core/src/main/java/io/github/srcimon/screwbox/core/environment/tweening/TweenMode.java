package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.UnaryOperator;

//TODO tests

/**
 * Configures the direction and the value change in a tween.
 */
public enum TweenMode {

    /**
     * Linear fade in: 0 to 1
     */
    LINEAR_IN(v -> v),

    /**
     * Linear fade out: 1 to 0
     */
    LINEAR_OUT(v -> v.invert()),

    /**
     * Sinus fade in: 0 to 1
     */
    SINE_IN(v -> Percent.of(Math.sin((v.value() * Math.PI) / 2.0))),

    /**
     * Sinus fade out: 1 to 0
     */
    SINE_OUT(v -> Percent.of(Math.cos((v.value() * Math.PI) / 2.0))),

    /**
     * Sinus fade in and out again: 0 to 1 to 0
     */
    SINE_IN_OUT(v -> Percent.of(-(Math.cos(Math.PI * v.value() * 2.0) - 1.0) / 2.0));

    private final UnaryOperator<Percent> adjustment;

    TweenMode(final UnaryOperator<Percent> adjustment) {
        this.adjustment = adjustment;
    }

    public Percent applyOn(final Percent input) {
        return adjustment.apply(input);
    }
}
