package dev.screwbox.core.graphics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScreenBoundsTest {

    @Test
    void move_noMotion_isSame() {
        ScreenBounds bounds = new ScreenBounds(30, 30, 100, 20);
        assertThat(bounds.move(0, 0)).isSameAs(bounds);
    }

    @Test
    void move_motion_hasUpdatedOffset() {
        ScreenBounds bounds = new ScreenBounds(30, 30, 100, 20);
        assertThat(bounds.move(4, 2)).isEqualTo(new ScreenBounds(34, 32, 100, 20));
    }

    @Test
    void intersects_doesntIntersect_isFalse() {
        ScreenBounds menuItemA = new ScreenBounds(30, 30, 100, 20);
        ScreenBounds menuItemB = new ScreenBounds(20, 50, 100, 20);

        assertThat(menuItemA.intersects(menuItemB)).isFalse();
    }

    @Test
    void intersects_intersects_isTrue() {
        ScreenBounds menuItemA = new ScreenBounds(30, 30, 100, 20);
        ScreenBounds menuItemB = new ScreenBounds(10, 30, 100, 20);

        assertThat(menuItemA.intersects(menuItemB)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"30,30", "130,50", "70,35"})
    void contains_positionInBounds_isTrue(int x, int y) {
        ScreenBounds bounds = new ScreenBounds(30, 30, 100, 20);

        assertThat(bounds.contains(Offset.at(x, y))).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"29,30", "130,200", "130,500"})
    void contains_positionOutOfBounds_isFalse(int x, int y) {
        ScreenBounds bounds = new ScreenBounds(30, 30, 100, 20);

        assertThat(bounds.contains(Offset.at(x, y))).isFalse();
    }

    @Test
    void snap_outOfGrid_movesInsideGrid() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);
        assertThat(bounds.snap(10)).isEqualTo(new ScreenBounds(30, 30, 108, 20));
    }

    @Test
    void x_returnsOffsetX() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);
        assertThat(bounds.x()).isEqualTo(31);
    }

    @Test
    void y_returnsOffsetY() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);
        assertThat(bounds.y()).isEqualTo(34);
    }

    @Test
    void maxX_returnsMaxX() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);
        assertThat(bounds.maxX()).isEqualTo(139);
    }

    @Test
    void maxY_returnsMaxY() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);
        assertThat(bounds.maxY()).isEqualTo(54);
    }

    @Test
    void expand_validExpansion_returnsBiggerBounds() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);

        ScreenBounds expanded = bounds.expand(10);

        assertThat(expanded).isEqualTo(new ScreenBounds(26, 29, 118, 30));
    }

    @Test
    void expand_invalidExpansion_throwsException() {
        ScreenBounds bounds = new ScreenBounds(31, 34, 108, 20);

        assertThatThrownBy(() -> bounds.expand(-100))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void around_noPositions_throwsException() {
        List<Offset> noPositions = Collections.emptyList();
        assertThatThrownBy(() -> ScreenBounds.around(noPositions))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("positions must not be empty");
    }

    @Test
    void around_somePositions_createsBoundsAroundPositions() {
        var bounds = ScreenBounds.around(List.of(Offset.at(4, 2), Offset.at(-100, 99), Offset.at(400, 3)));
        assertThat(bounds).isEqualTo(new ScreenBounds(-100, 2, 500, 97));
    }
}
