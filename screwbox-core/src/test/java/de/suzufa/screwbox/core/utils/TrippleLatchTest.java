package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrippleLatchTest {

    TrippleLatch<String> latch;

    @BeforeEach
    void beforeEach() {
        latch = TrippleLatch.of("A", "B", "C");
    }

    @Test
    void primary_notSwapped_returnsFirstValue() {
        assertThat(latch.primary()).isEqualTo("A");
    }

    @Test
    void backup_notSwapped_returnsSecondValue() {
        assertThat(latch.backup()).isEqualTo("B");
    }

    @Test
    void secondaryBackup_notSwapped_returnsThirdValue() {
        assertThat(latch.secondaryBackup()).isEqualTo("C");
    }

    @Test
    void primary_swappedOnce_returnsThirdValue() {
        latch.swap();

        assertThat(latch.primary()).isEqualTo("C");
    }

    @Test
    void primary_swappedTwicde_returnsSecondValue() {
        latch.swap();
        latch.swap();

        assertThat(latch.primary()).isEqualTo("B");
    }

    @Test
    void backup_swappedOnce_returnsFirstValue() {
        latch.swap();

        assertThat(latch.backup()).isEqualTo("A");
    }

    @Test
    void backup_swappedTwice_returnsThirdValue() {
        latch.swap();
        latch.swap();

        assertThat(latch.backup()).isEqualTo("C");
    }

    @Test
    void secondaryBackup_swappedFoutTimes_returnsSecondValue() {
        latch.swap();
        latch.swap();
        latch.swap();
        latch.swap();

        assertThat(latch.secondaryBackup()).isEqualTo("B");
    }
}
