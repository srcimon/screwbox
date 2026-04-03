package dev.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SizeableTest {

    Sizeable sizeable;

    @BeforeEach
    void setUp() {
        sizeable = () -> Size.of(20, 40);
    }

    @Test
    void width_returnsWidth() {
        assertThat(sizeable.width()).isEqualTo(20);
    }

    @Test
    void height_returnsHeight() {
        assertThat(sizeable.height()).isEqualTo(40);
    }

    @Test
    void center_returnsCenter() {
        assertThat(sizeable.center()).isEqualTo(Offset.at(10, 20));
    }
}
