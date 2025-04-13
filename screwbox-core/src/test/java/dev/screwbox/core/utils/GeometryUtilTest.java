package dev.screwbox.core.utils;

import dev.screwbox.core.Bounds;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryUtilTest {

    @Test
    void tryToCombine_notAligned_returnsEmptyOptional() {
        Bounds wall = Bounds.atOrigin(0, 0, 10, 10);
        Bounds door = Bounds.atOrigin(20, 0, 20, 10);

        Optional<Bounds> result = GeometryUtil.tryToCombine(wall, door);

        assertThat(result).isEmpty();
    }

    @Test
    void tryToCombine_alignedWithSameHeight_returnsCombined() {
        Bounds wall = Bounds.atOrigin(0, 0, 10, 10);
        Bounds door = Bounds.atOrigin(10, 0, 20, 10);

        Optional<Bounds> result = GeometryUtil.tryToCombine(wall, door);

        assertThat(result).isEqualTo(Optional.of(Bounds.atOrigin(0, 0, 30, 10)));
    }

    @Test
    void tryToCombine_alignedWithSameWith_returnsCombined() {
        Bounds wall = Bounds.atOrigin(0, 15, 20, 15);
        Bounds door = Bounds.atOrigin(0, 0, 20, 15);

        Optional<Bounds> result = GeometryUtil.tryToCombine(wall, door);

        assertThat(result).isEqualTo(Optional.of(Bounds.atOrigin(0, 0, 20, 30)));
    }
}
