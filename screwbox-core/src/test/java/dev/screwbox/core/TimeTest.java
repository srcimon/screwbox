package dev.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeTest {

    @Test
    void add_unitMinutes_addsMinutes() {
        var time = Time.atNanos(400);

        var result = time.add(1, Time.Unit.MINUTES);

        assertThat(result.nanos()).isEqualTo(60000000400L);
    }

    @Test
    void now_returnsTimeOfNow() {
        long before = System.nanoTime();
        Time now = Time.now();
        long after = System.nanoTime();

        assertThat(now.nanos()).isBetween(before, after);
    }

    @Test
    void atNanos_returnsSpecificTime() {
        Time time = Time.atNanos(50 * Time.Unit.MILLISECONDS.nanos());

        assertThat(time.milliseconds()).isEqualTo(50);
    }

    @Test
    void addSeconds_addsSecondsToGivenTime() {
        Time time = Time.atNanos(20_000);

        Time later = time.addSeconds(2);

        assertThat(later.nanos()).isEqualTo(2_000_020_000);
    }

    @Test
    void addMillis_addsMillisecondsToGivenTime() {
        Time time = Time.atNanos(20_000);

        Time later = time.addMillis(50);

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
}
