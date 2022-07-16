package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
    void substract_returnsNewOffset() {
        Offset offset = Offset.at(5, 30);

        Offset result = offset.substract(Offset.at(4, 3));

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
}
