package dev.screwbox.core.utils;

import dev.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

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
}
