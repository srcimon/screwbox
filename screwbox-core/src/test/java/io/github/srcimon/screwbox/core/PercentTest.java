package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class PercentTest {

    @ParameterizedTest
    @CsvSource({
            "0.9,0.2,0.1",
            "0.9,1.2,0.1",
            "0.9,2.2,0.1",
            "0.9,0.0,0.9",
            "0.1,-0.2,0.9",
            "0.1,-1.2,0.9",
            "0.1,-2.2,0.9",
            "0.1,-4.2,0.9",
            "0.1,4.2,0.3",
            "0.9,0.9,0.8"})
    void addWithOverflow_valueAddsResultsInOverflow_returnsValidResult(double initial, double value, double expected) {
        var result = Percent.of(initial).addWithOverflow(value);
        assertThat(result.value()).isEqualTo(expected, offset(0.001));
    }

    @Test
    void of_valueOutOfRange_returnsClampedValue() {
        assertThat(Percent.of(5).value()).isEqualTo(1);
        assertThat(Percent.of(-2).value()).isZero();
    }

    @Test
    void isZero_valueZero_returnsTrue() {
        assertThat(Percent.of(0).isZero()).isTrue();
    }

    @Test
    void isZero_valueAboveZero_returnsFalse() {
        assertThat(Percent.of(0.1).isZero()).isFalse();
    }

    @Test
    void isMaxValue_valueMax_returnsTrue() {
        assertThat(Percent.of(1).isMax()).isTrue();
    }

    @Test
    void isMaxValue_valueLowerThanMax_returnsTrue() {
        assertThat(Percent.of(0.8).isMax()).isFalse();
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

        assertThat(result).isEqualTo(Percent.zero());
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
    void invert_valueIsZero_returnsMaxValue() {
        Percent original = Percent.zero();

        assertThat(original.invert().isMax()).isTrue();
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

    @ParameterizedTest
    @CsvSource({
            "0.2,  0, 10,2",
            "0.2,  4, 10,5",
            "0.5, -4, 4,0"})
    void rangeValue_intRange_returnsValueFromRange(double value, int from, int to, int expectation) {
        var result = Percent.of(value).rangeValue(from, to);
        assertThat(result).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({
            "0.2,  0.0,  10.0,   2.0",
            "0.2,  4.4, -40.0, -4.48",
            "0.5, -4.0,   4,0,   0.0"})
    void rangeValue_doubleRange_returnsValueFromRange(double value, double from, double to, double expectation) {
        var result = Percent.of(value).rangeValue(from, to);
        assertThat(result).isEqualTo(expectation);
    }
}

