package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TimeTest {

    @Test
    void now_returnsTimeOfNow() {
        long before = System.nanoTime();
        Time now = Time.now();
        long after = System.nanoTime();

        assertThat(now.nanos()).isBetween(before, after);
    }

    @Test
    void atNanos_returnsSpecificTime() {
        Time time = Time.atNanos(50 * Time.NANOS_PER_MILLISECOND);

        assertThat(time.milliseconds()).isEqualTo(50);
    }

    @Test
    void plusSeconds_addsSecondsToGivenTime() {
        Time time = Time.atNanos(20_000);

        Time later = time.plusSeconds(2);

        assertThat(later.nanos()).isEqualTo(2_000_020_000);
    }

    @Test
    void plusMillis_addsMillisecondsToGivenTime() {
        Time time = Time.atNanos(20_000);

        Time later = time.plusMillis(50);

        assertThat(later.nanos()).isEqualTo(50_020_000);
    }

    @Test
    void isAfter_otherIsUnset_isFalse() {
        assertThat(Time.now().isAfter(Time.unset())).isFalse();
    }

    @Test
    void isAfter_timeIsBefore_returnsFalse() {
        Time morning = Time.atNanos(10_000);
        Time evening = Time.atNanos(20_000);

        assertThat(morning.isAfter(evening)).isFalse();
    }

    @Test
    void isAfter_timeIsLater_returnsTrue() {
        Time morning = Time.atNanos(10_000);
        Time evening = Time.atNanos(20_000);

        assertThat(evening.isAfter(morning)).isTrue();
    }

    @Test
    void isUnset_timeNotSet_returnsTrue() {
        Time time = Time.atNanos(0);

        assertThat(time.isUnset()).isTrue();
    }

    @Test
    void isUnset_timeSet_returnsFalse() {
        Time time = Time.atNanos(24);

        assertThat(time.isUnset()).isFalse();
    }

    @Test
    void isSet_timeNotSet_returnsFalse() {
        Time time = Time.atNanos(0);

        assertThat(time.isSet()).isFalse();
    }

    @Test
    void isSet_timeSet_returnsTrue() {
        Time time = Time.atNanos(24);

        assertThat(time.isSet()).isTrue();
    }

    @Test
    void plus_addsDuration() {
        Time evening = Time.atNanos(40_000);
        Time later = evening.plus(Duration.ofMillis(20));

        assertThat(later.nanos()).isEqualTo(20_040_000);
    }
}
