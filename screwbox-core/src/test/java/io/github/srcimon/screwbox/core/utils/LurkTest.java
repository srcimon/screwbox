package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

class LurkTest {

    private Lurk lurk;

    @BeforeEach
    void setUp() {
        lurk = Lurk.intervalWithDeviation(ofSeconds(1), Percent.quater());
    }

    @RepeatedTest(4)
    void value_returnsValueInRange() {
        assertThat(lurk.value(Time.now())).isBetween(-1.0, 1.0);
    }

}
