package dev.screwbox.core.utils;

import dev.screwbox.core.Bounds;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Bounds.$$;
import static org.assertj.core.api.Assertions.assertThat;

class PixelperfectTest {

    @Test
    void bounds_notPixelperfect_returnsPixelperfectBounds() {
        Bounds bounds = $$(12.123, 1239.40, 129.60, 132.40);

        var result = Pixelperfect.bounds(bounds);

        assertThat(result).isEqualTo($$(12, 1238, 130.0, 134.0));
    }
}
