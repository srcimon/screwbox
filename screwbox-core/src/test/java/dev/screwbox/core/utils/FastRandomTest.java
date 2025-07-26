package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FastRandomTest {

    @Test
    void nextBoolean_sameSeed_sameResult() {
        var first = new FastRandom(12).nextBoolean();
        var second = new FastRandom(12).nextBoolean();

        assertThat(first).isEqualTo(second);
    }

}
