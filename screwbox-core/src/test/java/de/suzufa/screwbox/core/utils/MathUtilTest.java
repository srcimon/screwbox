package de.suzufa.screwbox.core.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathUtilTest {

    @Test
    void clamp_valueIsBelowMin_returnsMin() {
        float clamp = MathUtil.clamp(1, -4, 5);

        assertThat(clamp).isEqualTo(1);
    }

    @Test
    void clamp_valueIsAboveMax_returnsMax() {
        float clamp = MathUtil.clamp(1, 6, 5);

        assertThat(clamp).isEqualTo(5);
    }

    @Test
    void clamp_valueIsInRange_returnsValue() {
        float clamp = MathUtil.clamp(1, 2, 5);

        assertThat(clamp).isEqualTo(2);
    }

    @Test
    void clamp_floatVlueIsBelowMin_returnsMin() {
        float clamp = MathUtil.clamp(1f, -4f, 5f);

        assertThat(clamp).isEqualTo(1f);
    }

    @Test
    void clamp_floatValueIsAboveMax_returnsMax() {
        float clamp = MathUtil.clamp(1f, 6f, 5f);

        assertThat(clamp).isEqualTo(5f);
    }

    @Test
    void clamp_floatValueIsInRange_returnsValue() {
        float clamp = MathUtil.clamp(1f, 2f, 5f);

        assertThat(clamp).isEqualTo(2f);
    }

    @Test
    void haveSameSign_negativeAndPositive_returnsFalse() {
        assertThat(MathUtil.haveSameSign(1, -1)).isFalse();
        assertThat(MathUtil.haveSameSign(-1, 5)).isFalse();
        assertThat(MathUtil.haveSameSign(-1, 0)).isFalse();
    }

    @Test
    void haveSameSign_bothZero_returnsTrue() {
        assertThat(MathUtil.haveSameSign(0, 0)).isTrue();
    }

    @Test
    void haveSameSign_bothSameSign_returnsTrue() {
        assertThat(MathUtil.haveSameSign(220, 9)).isTrue();
        assertThat(MathUtil.haveSameSign(-4, -20)).isTrue();
    }

    @Test
    void modifier_numberIsPositive_returnsOne() {
        assertThat(MathUtil.modifier(0.1)).isEqualTo(1);
    }

    @Test
    void modifier_numberIsNegative_returnsMinusOne() {
        assertThat(MathUtil.modifier(-990.1)).isEqualTo(-1);
    }

}
