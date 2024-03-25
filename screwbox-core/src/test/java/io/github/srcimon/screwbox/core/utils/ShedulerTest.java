package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShedulerTest {

    Sheduler sheduler;

    @BeforeEach
    void beforeEach() {
        sheduler = Sheduler.withInterval(Duration.ofSeconds(5));
    }

    @Test
    void isTick_initialy_isTrue() {
        boolean isTick = sheduler.isTick(Time.now());

        assertThat(isTick).isTrue();
    }

    @Test
    void isTick_later_isTrueAndResetsTimer() {
        Time later = Time.now().addSeconds(20);

        assertThat(sheduler.isTick(later)).isTrue();
        assertThat(sheduler.isTick(later)).isFalse();
    }

    @Test
    void isTick_noArgument_returnsTickStatus() {
        assertThat(sheduler.isTick()).isTrue();
        assertThat(sheduler.isTick()).isFalse();
    }

    @Test
    void hashcode_calculatesHashCode() {
        Time aTime = Time.atNanos(4000);
        Time anotherTime = Time.unset();

        assertThat(aTime.hashCode()).isEqualTo(4031);
        assertThat(aTime).doesNotHaveSameHashCodeAs(anotherTime);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Time.atNanos(50)).isEqualTo(Time.atNanos(50));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Time.atNanos(4000)).isNotEqualTo(Time.atNanos(4001));
    }

}
