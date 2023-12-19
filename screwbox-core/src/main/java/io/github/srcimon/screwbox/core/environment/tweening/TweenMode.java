package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.UnaryOperator;

//TODO javadoc and tests
//TODO Maybe better a class with static values
public enum TweenMode {

    LINEAR_IN(v -> v),
    LINEAR_OUT(v -> v.invert()),
    SINE_IN(v -> Percent.of(Math.sin((v.value() * Math.PI) / 2.0))),
    SINE_OUT(v -> Percent.of(Math.cos((v.value() * Math.PI) / 2.0))),
    SINE_IN_OUT(v -> Percent.of(-(Math.cos(Math.PI * v.value() * 2.0) - 1.0) / 2.0));

    private final UnaryOperator<Percent> adjustment;

    TweenMode(final UnaryOperator<Percent> adjustment) {
        this.adjustment = adjustment;
    }

    public static void main(String[] args) {
        for (double i = 0; i < 1; i += 0.1) {
            System.out.println(SINE_IN_OUT.adjustment.apply(Percent.of(i)).value());
        }
    }
}
