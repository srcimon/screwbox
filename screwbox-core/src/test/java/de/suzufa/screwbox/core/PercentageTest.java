package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PercentageTest {

    @Test
    void of_valueOutOfRange_returnsClampedValue() {
        assertThat(Percentage.of(5).value()).isEqualTo(1);
        assertThat(Percentage.of(-2).value()).isZero();
    }

    @Test
    void isMinValue_valueMin_returnsTrue() {
        assertThat(Percentage.of(0).isMinValue()).isTrue();
    }

    @Test
    void isMinValue_valueAboveMin_returnsFalse() {
        assertThat(Percentage.of(0.1).isMinValue()).isFalse();
    }

    @Test
    void isMaxValue_valueMax_returnsTrue() {
        assertThat(Percentage.of(1).isMaxValue()).isTrue();
    }

    @Test
    void isMaxValue_valueLowertThanMax_returnsTrue() {
        assertThat(Percentage.of(0.8).isMaxValue()).isFalse();
    }

    @Test
    void substract_valueIsLessThanPercentage_reducesValue() {
        Percentage percentage = Percentage.of(0.8);
        Percentage result = percentage.substract(0.4);

        assertThat(result).isEqualTo(Percentage.of(0.4));
    }

    @Test
    void substract_valueIsReducedBelowMinValue_setsValueToMinValue() {
        Percentage percentage = Percentage.of(0.2);
        Percentage result = percentage.substract(0.7);

        assertThat(result).isEqualTo(Percentage.min());
    }

    @Test
    void add_sumIsLessThanMax_addsValue() {
        Percentage percentage = Percentage.of(0.3);
        Percentage result = percentage.add(0.4);

        assertThat(result).isEqualTo(Percentage.of(0.7));
    }

    @Test
    void add_sumIsMoreThanMax_returnsMax() {
        Percentage percentage = Percentage.of(0.5);
        Percentage result = percentage.add(0.7);

        assertThat(result).isEqualTo(Percentage.max());
    }

    @Test
    void invert_valueIsMin_returnsMaxValue() {
        Percentage original = Percentage.min();

        assertThat(original.invert().isMaxValue()).isTrue();
    }

    @Test
    void invert_valueSet_returnsPercentageWithInvertedValue() {
        Percentage original = Percentage.of(0.3);

        assertThat(original.invert()).isEqualTo(Percentage.of(0.7));
    }

    @Test
    void hashcode_calculatesHashCode() {
        Percentage aPercentage = Percentage.of(0.4);
        Percentage anotherPercentage = Percentage.of(0.41);

        assertThat(aPercentage.hashCode()).isEqualTo(-1505755102);
        assertThat(anotherPercentage).doesNotHaveSameHashCodeAs(aPercentage);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Percentage.of(0.4)).isEqualTo(Percentage.of(0.4));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Percentage.of(0.4)).isNotEqualTo(Percentage.of(0.6));
    }
}
