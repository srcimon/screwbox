package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Percentage.withPercentage;

class VectorTest {

    @Test
    void isZero_noLength_isTrue() {
        assertThat(Vector.$(0, 0).isZero()).isTrue();
        assertThat(Vector.zero().isZero()).isTrue();
    }

    @Test
    void isZero_hasLength_isFalse() {
        assertThat(Vector.$(0.1, 0).isZero()).isFalse();
        assertThat(Vector.zero().addY(2).isZero()).isFalse();
    }

    @Test
    void zero_returnsNullVector() {
        assertThat(Vector.zero()).isEqualTo(Vector.$(0, 0));
    }

    @Test
    void addX_addsX() {
        Vector vector = Vector.$(4, 6);

        Vector result = vector.addX(2.5);

        assertThat(result).isEqualTo(Vector.$(6.5, 6));
    }

    @Test
    void addY_addsY() {
        Vector vector = Vector.$(4, 6);

        Vector result = vector.addY(2.5);

        assertThat(result).isEqualTo(Vector.$(4, 8.5));
    }

    @Test
    void add_addsXandYcomponents() {
        Vector other = Vector.$(2.5, -1.1);
        Vector vector = Vector.$(4, 6);

        Vector result = vector.add(other);

        assertThat(result).isEqualTo(Vector.$(6.5, 4.9));
    }

    @Test
    void toString_returnsReadableVector() {
        String result = Vector.$(2, -4.12395).toString();

        assertThat(result).isEqualTo("Vector [x=2.00, y=-4.12]");
    }

    @Test
    void equals_vectorsHaveDifferentValues_isFalse() {
        Vector positionOne = Vector.$(2, 1);
        Vector positionTwo = Vector.$(5, 1);

        assertThat(positionOne).isNotEqualTo(positionTwo);
    }

    @Test
    void equals_vectorsHaveSameValues_isTrue() {
        Vector positionOne = Vector.$(1, 134);
        Vector positionTwo = Vector.$(1, 134);

        assertThat(positionOne).isEqualTo(positionTwo);
    }

    @Test
    void multiply_multipliesOriginalVector() {
        Vector original = Vector.$(10, -4);

        Vector product = original.multiply(-2);

        assertThat(product).isEqualTo(Vector.$(-20, 8));
    }

    @Test
    void x_returnsVectorWithXComponentOnly() {
        Vector vector = Vector.x(20);

        assertThat(vector).isEqualTo(Vector.$(20, 0));
    }

    @Test
    void y_returnsVectorWithYComponentOnly() {
        Vector vector = Vector.y(20);

        assertThat(vector).isEqualTo(Vector.$(0, 20));
    }

    @Test
    void distanceTo_noLength_returnsZero() {
        Vector from = Vector.$(2, 2);
        Vector to = Vector.$(2, 2);

        assertThat(from.distanceTo(to)).isZero();
    }

    @Test
    void distanceTo_differentPoints_returnsLength() {
        Vector from = Vector.$(2, 2);
        Vector to = Vector.$(20, 2);

        assertThat(from.distanceTo(to)).isEqualTo(18);
    }

    @Test
    void invert_invetsXAndYCoodinates() {
        Vector position = Vector.$(10, 5);

        Vector result = position.invert();

        assertThat(result).isEqualTo(Vector.$(-10, -5));
    }

    @Test
    void invertX_invetsXCoodinate() {
        Vector position = Vector.$(10, 5);

        Vector result = position.invertX();

        assertThat(result).isEqualTo(Vector.$(-10, 5));
    }

    @Test
    void invertY_invetsYCoodinate() {
        Vector position = Vector.$(10, 5);

        Vector result = position.invertY();

        assertThat(result).isEqualTo(Vector.$(10, -5));
    }

    @Test
    void substract_substractsOtherVector() {
        Vector original = Vector.$(20, 12);

        Vector result = original.substract(Vector.$(4, 2));

        assertThat(result).isEqualTo(Vector.$(16, 10));
    }

    @Test
    void hashcode_calculatesHashCode() {
        Vector aVector = Vector.$(20, 12);
        Vector anotherVector = Vector.$(12, 20);

        assertThat(aVector.hashCode()).isEqualTo(108266433);
        assertThat(aVector).doesNotHaveSameHashCodeAs(anotherVector);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Vector.$(20, 12)).isEqualTo(Vector.$(20, 12));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Vector.$(20, 12)).isNotEqualTo(Vector.$(12, 20));
    }

    @Test
    void length_lengthZero_returnsZero() {
        Vector vector = Vector.$(2, 9);
        Vector adjusted = vector.length(0);

        assertThat(adjusted).isEqualTo(Vector.zero());
        assertThat(adjusted.length()).isZero();
    }

    @Test
    void length_negativeLength_throwsException() {
        Vector vector = Vector.$(2, 9);

        assertThatThrownBy(() -> vector.length(-4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length must not be negative");
    }

    @Test
    void lenght_zeroVector_returnsUnchangedVector() {
        Vector result = Vector.zero().length(40);

        assertThat(result).isEqualTo(Vector.zero());
    }

    @Test
    void length_lengthGiven_returnsVectorWithNewLength() {
        Vector adjusted = Vector.$(2, 9).length(4);

        assertThat(adjusted.x()).isCloseTo(0.867, withPercentage(0.1));
        assertThat(adjusted.y()).isCloseTo(3.904, withPercentage(0.1));
        assertThat(adjusted.length()).isEqualTo(4);
    }
}
