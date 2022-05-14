package de.suzufa.screwbox.core.utils;

import static de.suzufa.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Time;

class TimerTest {

    Timer timer;

    @BeforeEach
    void beforeEach() {
        timer = Timer.withIntervalOf(ofSeconds(5));
    }

    @Test
    void isTick_tooEarly_false() {
        boolean isTick = timer.isTick(Time.now());

        assertThat(isTick).isFalse();
    }

    @Test
    void isTick_later_isTrueAndResetsTimer() {
        Time later = Time.now().plusSeconds(20);

        assertThat(timer.isTick(later)).isTrue();
        assertThat(timer.isTick(later)).isFalse();
    }
}
