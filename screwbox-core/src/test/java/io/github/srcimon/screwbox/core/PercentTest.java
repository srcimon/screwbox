package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PercentTest {

    @Test
    void of_valueOutOfRange_returnsClampedValue() {
        assertThat(Percent.of(5).value()).isEqualTo(1);
        assertThat(Percent.of(-2).value()).isZero();
    }

    @Test
    void isMinValue_valueMin_returnsTrue() {
        assertThat(Percent.of(0).isMinValue()).isTrue();
    }

    @Test
    void isMinValue_valueAboveMin_returnsFalse() {
        assertThat(Percent.of(0.1).isMinValue()).isFalse();
    }

    @Test
    void isMaxValue_valueMax_returnsTrue() {
        assertThat(Percent.of(1).isMaxValue()).isTrue();
    }

    @Test
    void isMaxValue_valueLowertThanMax_returnsTrue() {
        assertThat(Percent.of(0.8).isMaxValue()).isFalse();
    }

    @Test
    void substract_valueIsLessThanPercentage_reducesValue() {
        Percent percentage = Percent.of(0.8);
        Percent result = percentage.substract(0.4);

        assertThat(result).isEqualTo(Percent.of(0.4));
    }

    @Test
    void substract_valueIsReducedBelowMinValue_setsValueToMinValue() {
        Percent percentage = Percent.of(0.2);
        Percent result = percentage.substract(0.7);

        assertThat(result).isEqualTo(Percent.min());
    }

    @Test
    void add_sumIsLessThanMax_addsValue() {
        Percent percentage = Percent.of(0.3);
        Percent result = percentage.add(0.4);

        assertThat(result).isEqualTo(Percent.of(0.7));
    }

    @Test
    void add_sumIsMoreThanMax_returnsMax() {
        Percent percentage = Percent.of(0.5);
        Percent result = percentage.add(0.7);

        assertThat(result).isEqualTo(Percent.max());
    }

    @Test
    void invert_valueIsMin_returnsMaxValue() {
        Percent original = Percent.min();

        assertThat(original.invert().isMaxValue()).isTrue();
    }

    @Test
    void invert_valueSet_returnsPercentageWithInvertedValue() {
        Percent original = Percent.of(0.3);

        assertThat(original.invert()).isEqualTo(Percent.of(0.7));
    }

    @Test
    void hashcode_calculatesHashCode() {
        Percent aPercentage = Percent.of(0.4);
        Percent anotherPercentage = Percent.of(0.41);

        assertThat(aPercentage.hashCode()).isEqualTo(-1505755102);
        assertThat(anotherPercentage).doesNotHaveSameHashCodeAs(aPercentage);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Percent.of(0.4)).isEqualTo(Percent.of(0.4));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Percent.of(0.4)).isNotEqualTo(Percent.of(0.6));
    }

    @Test
    void multiply_valueInRange_returnsMultiplied() {
        Percent result = Percent.of(0.2).multiply(2);

        assertThat(result).isEqualTo(Percent.of(0.4));
    }
}
