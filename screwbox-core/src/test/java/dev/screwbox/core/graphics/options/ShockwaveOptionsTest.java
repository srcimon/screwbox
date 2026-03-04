package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShockwaveOptionsTest {

    @Test
    void radius_isNegative_throwsException() {
        assertThatThrownBy(() -> ShockwaveOptions.radius(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("radius must be in range 2 to 4096 (actual value: -1.0)");
    }

    @Test
    void intensity_isNegative_throwsException() {
        var options = ShockwaveOptions.radius(40);
        assertThatThrownBy(() -> options.intensity(-100))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("intensity must be positive (actual value: -100.0)");
    }

    @Test
    void width_isZero_throwsException() {
        var options = ShockwaveOptions.radius(40);
        assertThatThrownBy(() -> options.width(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("width must be positive (actual value: 0.0)");
    }

    @Test
    void newInstance_allPropertiesSet_hasCorrectValues() {
        var options = ShockwaveOptions.radius(40)
            .growth(Percent.of(0.4))
            .intensity(10)
            .duration(Duration.ofSeconds(4))
            .width(30);

        assertThat(options.radius()).isEqualTo(40);
        assertThat(options.intensity()).isEqualTo(10);
        assertThat(options.width()).isEqualTo(30);
        assertThat(options.growth()).isEqualTo(Percent.of(0.4));
    }
}
