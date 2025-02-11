package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulerTest {

    Scheduler scheduler;

    @BeforeEach
    void beforeEach() {
        scheduler = Scheduler.withInterval(Duration.ofSeconds(5));
    }

    @Test
    void everyMinute_createsSchedulerWithOneMinuteInterval() {
        Scheduler oneMinuteIntervalScheduler = Scheduler.everyMinute();

        assertThat(oneMinuteIntervalScheduler.interval()).isEqualTo(Duration.ofSeconds(60));
    }

    @Test
    void interval_returnsConfiguredInterval() {
        assertThat(scheduler.interval()).isEqualTo(Duration.ofSeconds(5));
    }

    @Test
    void isTick_initially_isTrue() {
        boolean isTick = scheduler.isTick(Time.now());

        assertThat(isTick).isTrue();
    }

    @Test
    void isTick_later_isTrueAndResetsTimer() {
        Time later = Time.now().addSeconds(20);

        assertThat(scheduler.isTick(later)).isTrue();
        assertThat(scheduler.isTick(later)).isFalse();
    }

    @Test
    void isTick_noArgument_returnsTickStatus() {
        assertThat(scheduler.isTick()).isTrue();
        assertThat(scheduler.isTick()).isFalse();
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
