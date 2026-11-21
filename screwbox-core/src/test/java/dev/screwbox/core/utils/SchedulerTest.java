package dev.screwbox.core.utils;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchedulerTest {

    Scheduler scheduler;

    @BeforeEach
    void beforeEach() {
        scheduler = Scheduler.ofInterval(Duration.ofSeconds(5));
    }

    @Test
    void ofRandomMillis_validInterval_returnsScheduler() {
        Scheduler randomScheduler = Scheduler.ofRandomMillis(600, 1400);

        assertThat(randomScheduler.interval()).isEqualTo(Duration.ofMillis(1000));
        assertThat(randomScheduler.jitter()).isEqualTo(Duration.ofMillis(400));
    }

    @Test
    void ofRandomMillis_minIntervalLongerThanMaxInterval_throwsException() {
        assertThatThrownBy(() -> Scheduler.ofRandomMillis(1400, 600))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("max interval must be at least or equal to min interval");
    }

    @Test
    void ofRandomSeconds_validInterval_returnsScheduler() {
        Scheduler randomScheduler = Scheduler.ofRandomSeconds(4, 8);

        assertThat(randomScheduler.interval()).isEqualTo(Duration.ofSeconds(6));
        assertThat(randomScheduler.jitter()).isEqualTo(Duration.ofSeconds(2));
    }

    @Test
    void ofMinutes_twoMinutes_createsScheduler() {
        Scheduler oneMinuteIntervalScheduler = Scheduler.ofMinutes(2);

        assertThat(oneMinuteIntervalScheduler.interval()).isEqualTo(Duration.ofSeconds(120));
    }

    @Test
    void ofSeconds_tenSeconds_createsScheduler() {
        Scheduler oneMinuteIntervalScheduler = Scheduler.ofSeconds(10);

        assertThat(oneMinuteIntervalScheduler.interval()).isEqualTo(Duration.ofSeconds(10));
        assertThat(oneMinuteIntervalScheduler.jitter().isNone()).isTrue();
    }

    @Test
    void ofMillis_hundredMillis_createsScheduler() {
        Scheduler oneMinuteIntervalScheduler = Scheduler.ofMillis(100);

        assertThat(oneMinuteIntervalScheduler.interval()).isEqualTo(Duration.ofMillis(100));
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
    void isTick_randomTimerAfterRandomInterval_isTrue() {
        var randomScheduler = Scheduler.ofRandomMillis(1000, 8000);

        assertThat(randomScheduler.isTick(Time.now())).isTrue(); // uncheck initial tick

        assertThat(randomScheduler.isTick(Time.now())).isFalse();
        assertThat(randomScheduler.isTick(Time.now().addSeconds(20))).isTrue();

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

    @Test
    void skipUntil_timeInFuture_noTickBefore() {
        Time targetTime = Duration.ofSeconds(10).addTo(Time.now());

        scheduler.skipUntil(targetTime);

        assertThat(scheduler.isTick()).isFalse();
    }

    @Test
    void skipUntil_timeInPast_leavesSchedulerUnchanged() {
        Time targetTime = Duration.ofSeconds(-10).addTo(Time.now());

        scheduler.skipUntil(targetTime);

        assertThat(scheduler.isTick()).isTrue();
    }

}
