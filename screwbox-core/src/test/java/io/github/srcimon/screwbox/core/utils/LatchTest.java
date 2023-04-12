package io.github.srcimon.screwbox.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LatchTest {

    private Latch<String> latch;

    @BeforeEach
    void beforeEach() {
        latch = Latch.of("A", "B");
    }

    @Test
    void active_notToggled_returnsFirst() {
        assertThat(latch.active()).isEqualTo("A");
    }

    @Test
    void active_toggled_returnsSecond() {
        latch.toggle();

        assertThat(latch.active()).isEqualTo("B");
    }

    @Test
    void inactive_notToggled_returnsSecond() {
        assertThat(latch.inactive()).isEqualTo("B");
    }

    @Test
    void inactive_toggled_returnsFirst() {
        latch.toggle();

        assertThat(latch.inactive()).isEqualTo("A");
    }

    @Test
    void assignActive_toggled_assignesSecond() {
        latch.toggle();
        latch.assignActive("C");

        assertThat(latch.active()).isEqualTo("C");
        assertThat(latch.inactive()).isEqualTo("A");
    }

    @Test
    void assignActive_notToggled_assignesFist() {
        latch.assignActive("C");

        assertThat(latch.active()).isEqualTo("C");
        assertThat(latch.inactive()).isEqualTo("B");
    }
}
