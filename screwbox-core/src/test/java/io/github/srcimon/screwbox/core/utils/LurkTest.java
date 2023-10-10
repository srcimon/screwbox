package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Duration.ofNanos;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

class LurkTest {

    private Lurk lurk;

    @BeforeEach
    void setUp() {
        lurk = Lurk.fixedInterval(ofSeconds(1));
    }

    @RepeatedTest(4)
    void value_secondCall_isInRangeButNotZero() {
        lurk.value(Time.now());

        assertThat(lurk.value(Time.now())).isBetween(-1.0, 1.0).isNotEqualTo(0.0);
    }

    @Test
    void value_firstCall_isZero() {
        var start = Time.now();
        assertThat(lurk.value(start)).isZero();
    }
}
