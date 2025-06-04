package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import java.util.Random;

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
    void createRandomUsingMultipleSeeds_sameSeeds_createsSameRandom() {
        Random random = MathUtil.createRandomUsingMultipleSeeds(10, 4, 9);
        Random second = MathUtil.createRandomUsingMultipleSeeds(10, 4, 9);

        assertThat(random.nextLong()).isEqualTo(8494305244039576738L);
        assertThat(second.nextLong()).isEqualTo(8494305244039576738L);
    }

    @Test
    void createRandomUsingMultipleSeeds_distinctSeeds_createsDistinctRandom() {
        Random random = MathUtil.createRandomUsingMultipleSeeds(10, 4, 2);
        Random second = MathUtil.createRandomUsingMultipleSeeds(10, 9);

        assertThat(random.nextLong()).isEqualTo(-7140710767760219308L);
        assertThat(second.nextLong()).isEqualTo(-1190617913951922172L);
    }
}
