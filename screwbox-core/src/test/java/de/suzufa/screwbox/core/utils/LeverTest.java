package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeverTest {

    private Lever<String> lever;

    @BeforeEach
    void beforeEach() {
        lever = Lever.of("A", "B");
    }

    @Test
    void primary_notSwapped_returnsPrimary() {
        assertThat(lever.primary()).isEqualTo("A");
    }

    @Test
    void primary_swapped_returnsSecondary() {
        lever.gearNext();

        assertThat(lever.primary()).isEqualTo("B");
    }

    @Test
    void backup_notSwapped_returnsSecondary() {
        assertThat(lever.backup()).isEqualTo("B");
    }

    @Test
    void backup_swapped_returnsPrimary() {
        lever.gearNext();

        assertThat(lever.backup()).isEqualTo("A");
    }
}
