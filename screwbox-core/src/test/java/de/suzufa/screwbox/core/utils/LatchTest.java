package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LatchTest {

    private Latch<String> latch;

    @BeforeEach
    void beforeEach() {
        latch = Latch.of("A", "B");
    }

    @Test
    void primary_notSwapped_returnsPrimary() {
        assertThat(latch.primary()).isEqualTo("A");
    }

    @Test
    void primary_swapped_returnsSecondary() {
        latch.swap();

        assertThat(latch.primary()).isEqualTo("B");
    }

    @Test
    void backup_notSwapped_returnsSecondary() {
        assertThat(latch.backup()).isEqualTo("B");
    }

    @Test
    void backup_swapped_returnsPrimary() {
        latch.swap();

        assertThat(latch.backup()).isEqualTo("A");
    }
}
