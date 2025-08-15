package dev.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class VectorTest {

    @Test
    void random_twoCalls_returnsDifferentVectors() {
        assertThat(Vector.random(20)).isNotEqualTo(Vector.random(20));
    }

    @Test
    void random_length40_hasLength40() {
        assertThat(Vector.random(40).length()).isEqualTo(40, offset(0.01));
    }

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
    void x_returnsVectorWithXComponentOnly() {
        Vector vector = Vector.x(20);

        assertThat(vector).isEqualTo($(20, 0));
    }

    @Test
    void y_returnsVectorWithYComponentOnly() {
        Vector vector = Vector.y(20);

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
    void invertX_invertsXCoordinate() {
        Vector position = $(10, 5);

        Vector result = position.invertX();

        assertThat(result).isEqualTo($(-10, 5));
    }

    @Test
    void invertY_invertsYCoordinate() {
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
    void length_lengthZero_returnsZero() {
        Vector vector = $(2, 9);
        Vector adjusted = vector.length(0);

        assertThat(adjusted).isEqualTo(Vector.zero());
        assertThat(adjusted.length()).isZero();
    }

    @Test
    void length_negativeLength_throwsException() {
        Vector vector = $(2, 9);

        assertThatThrownBy(() -> vector.length(-4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length must not be negative");
    }

    @Test
    void length_zeroVector_returnsUnchangedVector() {
        Vector result = Vector.zero().length(40);

        assertThat(result).isEqualTo(Vector.zero());
    }

    @Test
    void length_lengthGiven_returnsVectorWithNewLength() {
        Vector adjusted = $(2, 9).length(4);

        assertThat(adjusted.x()).isCloseTo(0.867, withPercentage(0.1));
        assertThat(adjusted.y()).isCloseTo(3.904, withPercentage(0.1));
        assertThat(adjusted.length()).isEqualTo(4);
    }

    @Test
    void nearest_othersIsEmpty_isNull() {
        assertThat($(10, 5).nearestOf(emptyList())).isNull();
    }

    @Test
    void nearest_onlyOneEntry_isEntry() {
        List<Vector> others = List.of($(3, 2));

        assertThat($(2, 4).nearestOf(others)).isEqualTo($(3, 2));
    }

    @Test
    void nearest_multipleEntries_findsNearest() {
        List<Vector> others = List.of($(0, 0), $(200, 400), $(3, 2), $(19, 22));

        assertThat($(2, 4).nearestOf(others)).isEqualTo($(3, 2));
    }

    @Test
    void replaceX_newX_returnsSameY() {
        assertThat($(2, 9).replaceX(3)).isEqualTo($(3, 9));
    }

    @Test
    void replaceY_newY_returnsSameX() {
        assertThat($(2, 9).replaceY(3)).isEqualTo($(2, 3));
    }

    @Test
    void snap_outOfGrids_snapsToGrid() {
        assertThat($(2, 4).snap(4)).isEqualTo($(0, 4));
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,4",
            "2,0,2",
            "4,0,0",
            "1,1,3",
            "-1,3,5"
    })
    void reduce_sameForXandY_reducesLength(double reduce, double x, double y) {
        assertThat($(2, 4).reduce(reduce)).isEqualTo($(x, y));
    }

    @ParameterizedTest
    @CsvSource({
            "2,3,0,1",
            "1,-2,1,6",
            "9,9,0,0"
    })
    void reduce_distinctValuesForXandY_reducesLength(double reduceX, double reduceY, double x, double y) {
        assertThat($(2, 4).reduce(reduceX, reduceY)).isEqualTo($(x, y));
    }
}
