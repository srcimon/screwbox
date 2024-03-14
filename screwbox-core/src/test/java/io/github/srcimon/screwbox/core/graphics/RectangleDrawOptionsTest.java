package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RectangleDrawOptionsTest {

    @Test
    void filled_createsFilledOptions() {
        var options = RectangleDrawOptions.filled(Color.YELLOW).rotation(Rotation.degrees(45));

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.FILLED);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Rotation.degrees(45));
        assertThat(options.strokeWidth()).isEqualTo(1);
    }

    @Test
    void outline_createsOutlineOptions() {
        var options = RectangleDrawOptions.outline(Color.YELLOW).strokeWidth(5);

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.FILLED);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Rotation.none());
        assertThat(options.strokeWidth()).isEqualTo(5);
    }
}
