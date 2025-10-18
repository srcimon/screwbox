package dev.screwbox.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FastRandomTest {

    FastRandom fastRandom;

    @BeforeEach
    void setUp() {
        fastRandom = new FastRandom();
    }

    @Test
    void nextBoolean_sameSeed_sameResult() {
        var first = new FastRandom(12).nextBoolean();
        var second = new FastRandom(12).nextBoolean();

        assertThat(first).isEqualTo(second);
    }

    @Test
    void createUUID_aMillionIds_allAreUnique() {
        Set<UUID> ids = new HashSet<>();
        for (int i = 0; i < 1_000_000; i++) {
            ids.add(fastRandom.createUUID());
        }
        assertThat(ids).hasSize(1_000_000);
    }
}
