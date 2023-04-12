package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class WindowBoundsTest {

    @Test
    void intersects_dosntIntersect_isFalse() {
        WindowBounds menuItemA = new WindowBounds(30, 30, 100, 20);
        WindowBounds menuItemB = new WindowBounds(20, 50, 100, 20);

        assertThat(menuItemA.intersects(menuItemB)).isFalse();
    }

    @Test
    void intersects_intersects_isTrue() {
        WindowBounds menuItemA = new WindowBounds(30, 30, 100, 20);
        WindowBounds menuItemB = new WindowBounds(10, 30, 100, 20);

        assertThat(menuItemA.intersects(menuItemB)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({ "30,30", "130,50", "70,35" })
    void contains_positionInBounds_isTrue(int x, int y) {
        WindowBounds bounds = new WindowBounds(30, 30, 100, 20);

        assertThat(bounds.contains(Offset.at(x, y))).isTrue();
    }

    @ParameterizedTest
    @CsvSource({ "29,30", "130,200", "130,500" })
    void contains_positionOutOfBounds_isFalse(int x, int y) {
        WindowBounds bounds = new WindowBounds(30, 30, 100, 20);

        assertThat(bounds.contains(Offset.at(x, y))).isFalse();
    }
}
