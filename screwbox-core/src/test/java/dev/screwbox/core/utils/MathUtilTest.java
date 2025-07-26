package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MathUtilTest {

    @Test
    void sameSign_negativeAndPositive_returnsFalse() {
        assertThat(MathUtil.sameSign(1, -1)).isFalse();
        assertThat(MathUtil.sameSign(-1, 5)).isFalse();
        assertThat(MathUtil.sameSign(-1, 0)).isFalse();
    }

    @Test
    void sameSign_bothZero_returnsTrue() {
        assertThat(MathUtil.sameSign(0, 0)).isTrue();
    }

    @Test
    void sameSign_bothSameSign_returnsTrue() {
        assertThat(MathUtil.sameSign(220, 9)).isTrue();
        assertThat(MathUtil.sameSign(-4, -20)).isTrue();
    }

    @Test
    void modifier_numberIsPositive_returnsOne() {
        assertThat(MathUtil.modifier(0.1)).isEqualTo(1);
    }

    @Test
    void modifier_numberIsNegative_returnsMinusOne() {
        assertThat(MathUtil.modifier(-990.1)).isEqualTo(-1);
    }

    @Test
    void combineSeeds_sameSeeds_createsSameSeed() {
        long first = MathUtil.combineSeeds(10, 4, 9);
        long second = MathUtil.combineSeeds(10, 4, 9);

        assertThat(first).isEqualTo(second).isEqualTo(-1983069687856612883L);
    }

    @Test
    void combineSeeds_distinctSeeds_createsDistinctSeed() {
        long first = MathUtil.combineSeeds(10, 4, 2);
        long second = MathUtil.combineSeeds(10, 9);

        assertThat(first).isNotEqualTo(second).isEqualTo(-1983069687621079306L);
    }
}
