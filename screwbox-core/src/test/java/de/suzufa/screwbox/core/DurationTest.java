package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DurationTest {

    @Test
    void zero_returnsDurationOfNotEvenANanoSecond() {
        assertThat(Duration.zero().nanos()).isZero();
    }

    @Test
    void since_returnsDurationOfPositiveNanos() {
        Time now = Time.now();

        long nanosSince = Duration.since(now).nanos();

        assertThat(nanosSince).isNotNegative();
    }

    @Test
    void ofMillis_returnsNewInstance() {
        Duration duration = Duration.ofMillis(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.NANOS_PER_MILLISECOND);
    }

    @Test
    void ofNanos_returnsNewInstance() {
        Duration duration = Duration.ofNanos(5 * Time.NANOS_PER_MILLISECOND);

        assertThat(duration.milliseconds()).isEqualTo(5);
    }

    @Test
    void between_thenIsBeforeNow_returnsPositiveDuration() {
        Time now = Time.atNanos(20_000);
        Time then = Time.atNanos(10_500);

        assertThat(Duration.between(now, then)).isEqualTo(Duration.ofNanos(9_500));
    }

    @Test
    void between_nowIsBeforeThen_returnsPositiveDuration() {
        Time now = Time.atNanos(10_500);
        Time then = Time.atNanos(20_000);

        assertThat(Duration.between(now, then)).isEqualTo(Duration.ofNanos(9_500));
    }

    @Test
    void isAtLeast_otherHasLessNanos_returnsTrue() {
        Duration other = Duration.ofMillis(10);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isAtLeast(other)).isTrue();
    }

    @Test
    void isAtLeast_otherHasMoreNanos_returnsFalse() {
        Duration other = Duration.ofMillis(30);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isAtLeast(other)).isFalse();
    }

    @Test
    void isLessThan_otherHasLessNanos_returnsFalse() {
        Duration other = Duration.ofMillis(10);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isLessThan(other)).isFalse();
    }

    @Test
    void isLessThan_otherHasMoreNanos_returnsTrue() {
        Duration other = Duration.ofMillis(30);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isLessThan(other)).isTrue();
    }

    @Test
    void ofSeconds_returnsNewInstance() {
        Duration duration = Duration.ofSeconds(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.NANOS_PER_SECOND);
    }

}
