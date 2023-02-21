package io.github.simonbas.screwbox.core;

import org.junit.jupiter.api.Test;

import static io.github.simonbas.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

class VectorTest {

    @Test
    void isZero_noLength_isTrue() {
        assertThat($(0, 0).isZero()).isTrue();
        assertThat(Vector.zero().isZero()).isTrue();
    }

    @Test
    void isZero_hasLength_isFalse() {
        assertThat($(0.1, 0).isZero()).isFalse();
        assertThat(Vector.zero().addY(2).isZero()).isFalse();
    }

    @Test
    void zero_returnsNullVector() {
        assertThat(Vector.zero()).isEqualTo($(0, 0));
    }

    @Test
    void addX_addsX() {
        Vector vector = $(4, 6);

        Vector result = vector.addX(2.5);

        assertThat(result).isEqualTo($(6.5, 6));
    }

    @Test
    void addY_addsY() {
        Vector vector = $(4, 6);

        Vector result = vector.addY(2.5);

        assertThat(result).isEqualTo($(4, 8.5));
    }

    @Test
    void add_addsXandYcomponents() {
        Vector other = $(2.5, -1.1);
        Vector vector = $(4, 6);

        Vector result = vector.add(other);

        assertThat(result).isEqualTo($(6.5, 4.9));
    }

    @Test
    void toString_returnsReadableVector() {
        String result = $(2, -4.12395).toString();

        assertThat(result).isEqualTo("Vector [x=2.00, y=-4.12]");
    }

    @Test
    void equals_vectorsHaveDifferentValues_isFalse() {
        Vector positionOne = $(2, 1);
        Vector positionTwo = $(5, 1);

        assertThat(positionOne).isNotEqualTo(positionTwo);
    }

    @Test
    void equals_vectorsHaveSameValues_isTrue() {
        Vector positionOne = $(1, 134);
        Vector positionTwo = $(1, 134);

        assertThat(positionOne).isEqualTo(positionTwo);
    }

    @Test
    void multiply_multipliesOriginalVector() {
        Vector original = $(10, -4);

        Vector product = original.multiply(-2);

        assertThat(product).isEqualTo($(-20, 8));
    }

    @Test
    void xOnly_returnsVectorWithXComponentOnly() {
        Vector vector = Vector.xOnly(20);

        assertThat(vector).isEqualTo($(20, 0));
    }

    @Test
    void yOnly_returnsVectorWithYComponentOnly() {
        Vector vector = Vector.yOnly(20);

        assertThat(vector).isEqualTo($(0, 20));
    }

    @Test
    void distanceTo_noLength_returnsZero() {
        Vector from = $(2, 2);
        Vector to = $(2, 2);

        assertThat(from.distanceTo(to)).isZero();
    }

    @Test
    void distanceTo_differentPoints_returnsLength() {
        Vector from = $(2, 2);
        Vector to = $(20, 2);

        assertThat(from.distanceTo(to)).isEqualTo(18);
    }

    @Test
    void invert_invetsXAndYCoodinates() {
        Vector position = $(10, 5);

        Vector result = position.invert();

        assertThat(result).isEqualTo($(-10, -5));
    }

    @Test
    void invertX_invetsXCoodinate() {
        Vector position = $(10, 5);

        Vector result = position.invertX();

        assertThat(result).isEqualTo($(-10, 5));
    }

    @Test
    void invertY_invetsYCoodinate() {
        Vector position = $(10, 5);

        Vector result = position.invertY();

        assertThat(result).isEqualTo($(10, -5));
    }

    @Test
    void substract_substractsOtherVector() {
        Vector original = $(20, 12);

        Vector result = original.substract($(4, 2));

        assertThat(result).isEqualTo($(16, 10));
    }

    @Test
    void hashcode_calculatesHashCode() {
        Vector aVector = $(20, 12);
        Vector anotherVector = $(12, 20);

        assertThat(aVector.hashCode()).isEqualTo(108266433);
        assertThat(aVector).doesNotHaveSameHashCodeAs(anotherVector);
    }

    @Test
    void equals_same_isTrue() {
        assertThat($(20, 12)).isEqualTo($(20, 12));
    }

    @Test
    void equals_different_isFalse() {
        assertThat($(20, 12)).isNotEqualTo($(12, 20));
    }

    @Test
    void adjustLengthTo_lengthZero_returnsZero() {
        Vector vector = $(2, 9);
        Vector adjusted = vector.adjustLengthTo(0);

        assertThat(adjusted).isEqualTo(Vector.zero());
        assertThat(adjusted.length()).isZero();
    }

    @Test
    void adjustLengthTo_lengthGiven_returnsVectorWithNewLength() {
        Vector adjusted = $(2, 9).adjustLengthTo(4);

        assertThat(adjusted.x()).isCloseTo(0.867, withPercentage(0.1));
        assertThat(adjusted.y()).isCloseTo(3.904, withPercentage(0.1));
        assertThat(adjusted.length()).isEqualTo(4);
    }
}
