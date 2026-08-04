package dev.screwbox.core.utils;

import dev.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoiseTest {

    private Noise noise;

    @BeforeEach
    void setUp() {
        noise = Noise.fixedInterval(ofSeconds(1));
    }

    @RepeatedTest(4)
    void value_isInRangeButNotZero() {
        assertThat(noise.value(Time.now())).isBetween(-1.0, 1.0).isNotEqualTo(0.0);
    }

    @Test
    void fixedInterval_intervalNull_throwsException() {
        assertThatThrownBy(() -> Noise.fixedInterval(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("interval must not be null");
    }

    @Test
    void variableInterval_intervalNull_throwsException() {
        assertThatThrownBy(() -> Noise.variableInterval(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("interval must not be null");
    }
}
