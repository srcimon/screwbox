package dev.screwbox.core.graphics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class OffsetTest {

    @Test
    void at_returnsNewInstance() {
        Offset offset = Offset.at(0.0, 2.0);

        assertThat(offset.x()).isZero();
        assertThat(offset.y()).isEqualTo(2);
    }

    @Test
    void origin_returnsNewInstanceAtOrigin() {
        Offset offset = Offset.origin();

        assertThat(offset.x()).isZero();
        assertThat(offset.y()).isZero();
    }

    @Test
    void subtract_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);

        Offset result = offset.subtract(Offset.at(4, 3));

        assertThat(result).isEqualTo(Offset.at(1, 27));
    }

    @Test
    void add_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);

        Offset result = offset.add(Offset.at(4, 3));

        assertThat(result).isEqualTo(Offset.at(9, 33));
    }

    @Test
    void addX_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);

        Offset result = offset.addX(4);

        assertThat(result).isEqualTo(Offset.at(9, 30));
    }

    @Test
    void addY_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);

        Offset result = offset.addY(4);

        assertThat(result).isEqualTo(Offset.at(5, 34));
    }

    @Test
    void snap_outOfGrid_snapsToGrid() {
        Offset offset = Offset.at(5, 30);

        assertThat(offset.snap(16)).isEqualTo(Offset.at(0, 16));
    }

    @Test
    void snap_inGrid_unchanged() {
        Offset offset = Offset.at(16, 32);

        assertThat(offset.snap(16)).isEqualTo(offset);
    }

    @Test
    void snap_gridSizeZero_throwsException() {
        Offset offset = Offset.at(16, 32);
        assertThatThrownBy(() -> offset.snap(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("grid size must be positive (actual value: 0)");
    }

    @ParameterizedTest
    @CsvSource({
        "16, 32, 0",
        "-10, 4, 38.21",
        "16, 33, 1"})
    void distanceTo_differentArguments_returnsDistance(int x, int y, double distance) {
        Offset offset = Offset.at(16, 32);
        Offset other = Offset.at(x, y);
        assertThat(offset.distanceTo(other)).isEqualTo(distance, offset(0.01));
    }

    @Test
    void replaceX_newX_returnsSameY() {
        assertThat(Offset.at(2, 9).replaceX(3)).isEqualTo(Offset.at(3, 9));
    }

    @Test
    void replaceY_newY_returnsSameX() {
        assertThat(Offset.at(2, 9).replaceY(3)).isEqualTo(Offset.at(2, 3));
    }

    @Test
    void top_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.top()).isEqualTo(Offset.at(5, 29));
    }

    @Test
    void bottom_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.bottom()).isEqualTo(Offset.at(5, 31));
    }

    @Test
    void left_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.left()).isEqualTo(Offset.at(4, 30));
    }

    @Test
    void right_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.right()).isEqualTo(Offset.at(6, 30));
    }

    @Test
    void bottomLeft_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.bottomLeft()).isEqualTo(Offset.at(4, 31));
    }

    @Test
    void bottomRight_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.bottomRight()).isEqualTo(Offset.at(6, 31));
    }

    @Test
    void topLeft_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.topLeft()).isEqualTo(Offset.at(4, 29));
    }

    @Test
    void topRight_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);
        assertThat(offset.topRight()).isEqualTo(Offset.at(6, 29));
    }
}
