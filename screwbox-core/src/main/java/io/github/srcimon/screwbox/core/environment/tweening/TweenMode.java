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

    /**
     * Flickering effect. Mostly 1 but sometimes 0.
     *
     * @see #SPARKLE
     */
    FLICKER(in -> FLickerSupport.sequenceValue(in)),

    /**
     * Sparkling effect. Mostly 0 but sometimes 1.
     *
     * @see #FLICKER
     */
    SPARKLE(in -> FLickerSupport.sequenceValue(in).invert());

    private class FLickerSupport {
        private static final String FLICKER = "##########__1#######_##########################################################################___2233##########2##################################################5########################################################235#############55443#####################################555##";
        private static final Map<Character, Percent> TRANSLATION = Map.of(
                '_', Percent.zero(),
                '#', Percent.max(),
                '1', Percent.of(0.1),
                '2', Percent.of(0.2),
                '3', Percent.of(0.3),
                '4', Percent.of(0.4),
                '5', Percent.of(0.5));

        private static Percent sequenceValue(Percent progress) {
            final int sequencePosition = (int) (FLICKER.length() * progress.value() % FLICKER.length());
            return TRANSLATION.get(FLICKER.charAt(sequencePosition));
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
