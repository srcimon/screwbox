package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Configures the direction and the value change in a tween.
 */
public enum TweenMode {

    /**
     * Linear fade in: 0 to 1
     */
    LINEAR_IN(in -> in),

    /**
     * Linear fade out: 1 to 0
     */
    LINEAR_OUT(Percent::invert),

    /**
     * Sinus fade in: 0 to 1
     */
    SINE_IN(in -> Percent.of(Math.sin((in.value() * Math.PI) / 2.0))),

    /**
     * Sinus fade out: 1 to 0
     */
    SINE_OUT(in -> Percent.of(Math.cos((in.value() * Math.PI) / 2.0))),

    /**
     * Sinus fade in and out again: 0 to 1 to 0
     */
    SINE_IN_OUT(in -> Percent.of(-(Math.cos(Math.PI * in.value() * 2.0) - 1.0) / 2.0)),

    //TODO FIXUP
    FLICKER(in -> Sequences.FLICKER.valueAt(in));

    private enum Sequences {
        FLICKER("__________aab_______a__________________________________________________________________________aaaccdd__________c___________________________________________________________________________________________________________cdf_____________ffeed_____________________________________fff__");

        private final String sequenceValue;

        Sequences(final String sequenceValue) {
            this.sequenceValue = sequenceValue;

        }
         private Percent valueAt(final Percent position) {
            var charAt = sequenceValue.charAt((int) (sequenceValue.length() * position.value() % sequenceValue.length()));
            return Percent.of(Map.of(
                    '_', 1.0,
                    'a', 0.0,
                    'b', 0.1,
                    'c', 0.2,
                    'd', 0.3,
                    'e', 0.4,
                    'f', 0.5).get(charAt));
        }
    }

    private final UnaryOperator<Percent> adjustment;

    TweenMode(final UnaryOperator<Percent> adjustment) {
        this.adjustment = adjustment;
    }

    public Percent applyOn(final Percent input) {
        return adjustment.apply(input);
    }
}
