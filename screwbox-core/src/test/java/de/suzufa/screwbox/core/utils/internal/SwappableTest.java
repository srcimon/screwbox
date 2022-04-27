package de.suzufa.screwbox.core.utils.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwappableTest {

    private Swappable<String> swappable;

    @BeforeEach
    void beforeEach() {
        swappable = Swappable.of("A", "B");
    }

    @Test
    void primary_notSwapped_returnsPrimary() {
        assertThat(swappable.primary()).isEqualTo("A");
    }

    @Test
    void primary_swapped_returnsSecondary() {
        swappable.swap();

        assertThat(swappable.primary()).isEqualTo("B");
    }

    @Test
    void backup_notSwapped_returnsSecondary() {
        assertThat(swappable.backup()).isEqualTo("B");
    }

    @Test
    void backup_swapped_returnsPrimary() {
        swappable.swap();

        assertThat(swappable.backup()).isEqualTo("A");
    }
}
