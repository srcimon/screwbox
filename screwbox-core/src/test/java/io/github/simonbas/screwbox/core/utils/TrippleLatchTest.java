package io.github.simonbas.screwbox.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrippleLatchTest {

    TrippleLatch<String> latch;

    @BeforeEach
    void beforeEach() {
        latch = TrippleLatch.of("A", "B", "C");
    }

    @Test
    void active_notToggled_returnsFirstValue() {
        assertThat(latch.active()).isEqualTo("A");
    }

    @Test
    void inactive_notToggled_returnsSecondValue() {
        assertThat(latch.inactive()).isEqualTo("B");
    }

    @Test
    void backupInactive_notToggled_returnsThirdValue() {
        assertThat(latch.backupInactive()).isEqualTo("C");
    }

    @Test
    void active_toggledOnce_returnsThirdValue() {
        latch.toggle();

        assertThat(latch.active()).isEqualTo("C");
    }

    @Test
    void active_toggledTwice_returnsSecondValue() {
        latch.toggle();
        latch.toggle();

        assertThat(latch.active()).isEqualTo("B");
    }

    @Test
    void inactive_toggledOnce_returnsFirstValue() {
        latch.toggle();

        assertThat(latch.inactive()).isEqualTo("A");
    }

    @Test
    void inactive_toggledTwice_returnsThirdValue() {
        latch.toggle();
        latch.toggle();

        assertThat(latch.inactive()).isEqualTo("C");
    }

    @Test
    void backupInactive_toggledFourTimes_returnsSecondValue() {
        latch.toggle();
        latch.toggle();
        latch.toggle();
        latch.toggle();

        assertThat(latch.backupInactive()).isEqualTo("B");
    }
}
