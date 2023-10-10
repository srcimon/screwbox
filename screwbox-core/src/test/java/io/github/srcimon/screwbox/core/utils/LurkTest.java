package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

class LurkTest {

    private Lurk lurk;

    @BeforeEach
    void setUp() {
        lurk = Lurk.fixedInterval(ofSeconds(1));
    }
    @Test
    void value_returnsValueInRange() {
        assertThat(lurk.value(Time.now())).isBetween(-1.0, 1.0);
    }
}
