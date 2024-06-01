package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.github.srcimon.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationTest {

    @Test
    void seconds_returnsSeconds() {
        Duration seconds = Duration.ofSeconds(2);

        assertThat(seconds.seconds()).isEqualTo(2);
    }

    @Test
    void none_returnsDurationOfNotEvenANanoSecond() {
        assertThat(Duration.none().nanos()).isZero();
    }

    @Test
    void since_returnsDurationOfPositiveNanos() {
        Time now = now();

        long nanosSince = Duration.since(now).nanos();

        assertThat(nanosSince).isNotNegative();
    }

    @Test
    void ofMillis_returnsNewInstance() {
        Duration duration = Duration.ofMillis(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.Unit.MILLISECONDS.nanos());
    }

    @Test
    void ofMicros_returnsNewInstance() {
        Duration duration = Duration.ofMicros(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.Unit.MICROSECONDS.nanos());
    }

    @Test
    void ofNanos_returnsNewInstance() {
        Duration duration = Duration.ofNanos(5 * Time.Unit.MILLISECONDS.nanos());

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
        Duration duration = Duration.ofSeconds(12);

        assertThat(duration.nanos()).isEqualTo(12_000_000_000L);
    }

    @Test
    void oneSecond_returnsNewInstance() {
        Duration duration = Duration.oneSecond();

        assertThat(duration.nanos()).isEqualTo(1_000_000_000L);
    }

    @Test
    void hashcode_calculatesHashCode() {
        Duration aDuration = Duration.ofMillis(10);
        Duration anotherDuration = Duration.ofMillis(15);

        assertThat(aDuration.hashCode()).isEqualTo(10000031);
        assertThat(aDuration).doesNotHaveSameHashCodeAs(anotherDuration);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Duration.ofMillis(10)).isEqualTo(Duration.ofNanos(10_000_000));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Duration.ofMillis(10)).isNotEqualTo(Duration.ofMillis(3));
    }

    @Test
    void ofExecution_executionNull_throwsException() {
        assertThatThrownBy(() -> Duration.ofExecution(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("execution must not be null");
    }

    @Test
    void ofExecution_executionTakesTime_returnsNewInstance() {
        // burn some cpu
        Duration duration = Duration.ofExecution(Time::now);

        assertThat(duration.nanos()).isPositive();
    }

    @Test
    void add_valueSet_addsDuration() {
        Duration duration = Duration.ofSeconds(2);

        Duration result = duration.add(Duration.ofMillis(500));

        assertThat(result.milliseconds()).isEqualTo(2500);
    }

    @Test
    void isNone_hasLength_isFalse() {
        assertThat(Duration.ofSeconds(1).isNone()).isFalse();
    }

    @Test
    void isNone_noLength_isTrue() {
        assertThat(Duration.none().isNone()).isTrue();
    }

    @Test
    void add_valueNull_throwsException() {
        Duration duration = Duration.ofSeconds(2);
        assertThatThrownBy(() -> duration.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null");
    }

    @Test
    void addTo_validTime_returnsNewTime() {
        Time time = Time.atNanos(1239192);

        var result = Duration.ofSeconds(2).addTo(time);

        assertThat(result).isEqualTo(Time.atNanos(2001239192));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "0; 0ns",
            "20; 20ns",
            "21_000; 21µs",
            "92_921; 92µs, 921ns",
            "1_000_000; 1ms",
            "1_000_500; 1ms",
            "1_100_500; 1ms, 100µs",
            "34_000_000_000; 34s",
            "8_100_000_000_000; 2h, 15m",
            "8_100_777_666_555; 2h, 15m",
            "7_200_000_000_000; 2h",
            "7_200_000_000_999; 2h"
    })
    void humanReadable_validInput_returnsReadableString(long nanos, String humanFormat) {
        assertThat(Duration.ofNanos(nanos).humanReadable()).isEqualTo(humanFormat);
    }

    @Test
    void toString_isHumanReadable() {
        assertThat(Duration.ofNanos(8_100_000_000_000L)).hasToString("Duration [2h, 15m]");
    }

    @Test
    void progress_noDuration_isMax() {
        Duration.none().progress(now(), now()).isMax();
    }

    @Test
    void progress_4of10SecondsMissing_is60Percent() {
        Time started = Time.atNanos(10);
        Time now = started.addSeconds(6);
        var progress = Duration.ofSeconds(10).progress(started, now);

        assertThat(progress).isEqualTo(Percent.of(0.6));
    }

    @Test
    void progress_endReached_isMax() {
        Time started = Time.atNanos(10);
        Time now = started.addSeconds(60);
        var progress = Duration.ofSeconds(10).progress(started, now);

        assertThat(progress).isEqualTo(Percent.max());
    }
}
