package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WindowBoundsTest {

    @Test
    void intersects_dosntIntersect_isFalse() {
        WindowBounds menuItemA = new WindowBounds(Offset.at(30, 30), Dimension.of(100, 20));
        WindowBounds menuItemB = new WindowBounds(Offset.at(20, 50), Dimension.of(100, 20));

        assertThat(menuItemA.intersects(menuItemB)).isFalse();
    }

    @Test
    void intersects_intersects_isTrue() {
        WindowBounds menuItemA = new WindowBounds(Offset.at(30, 30), Dimension.of(100, 20));
        WindowBounds menuItemB = new WindowBounds(Offset.at(10, 30), Dimension.of(100, 20));

        assertThat(menuItemA.intersects(menuItemB)).isTrue();
    }
}
