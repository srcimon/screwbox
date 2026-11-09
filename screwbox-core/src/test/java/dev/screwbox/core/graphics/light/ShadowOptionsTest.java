package dev.screwbox.core.graphics.light;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShadowOptionsTest {

    @Test
    void newInstance_angular_setsOptions() {
        var options = ShadowOptions.angular()
            .affectOccluder()
            .distortion(Percent.of(0.8))
            .offset(Vector.$(10, 2))
            .backdropDistance(2.3);

        assertThat(options.isRounded()).isFalse();
        assertThat(options.backdropDistance()).isEqualTo(2.3);
        assertThat(options.isAffectOccluder()).isTrue();
        assertThat(options.offset()).isEqualTo(Vector.$(10, 2));
        assertThat(options.distortion()).isEqualTo(Percent.of(0.8));
    }

    @Test
    void newInstance_rounded_setsOptions() {
        var options = ShadowOptions.rounded();

        assertThat(options.isRounded()).isTrue();
        assertThat(options.backdropDistance()).isEqualTo(1.0);
        assertThat(options.isAffectOccluder()).isFalse();
        assertThat(options.offset().isZero()).isTrue();
        assertThat(options.distortion()).isEqualTo(Percent.zero());
    }

    @Test
    void backdropDistance_negative_throwsException() {
        var options = ShadowOptions.rounded();
        assertThatThrownBy(() -> options.backdropDistance(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("backdrop distance must be positive (actual value: -1.0)");
    }

    @Test
    void backdropDistance_zeroAndNoOffset_throwsException() {
        var options = ShadowOptions.rounded();
        assertThatThrownBy(() -> options.backdropDistance(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("backdrop distance can only be zero when offset is set");
    }

    @Test
    void backdropDistance_zeroAndOffset_setsOptions() {
        var options = ShadowOptions.rounded()
            .offset(Vector.$(10, 2))
            .backdropDistance(0);

        assertThat(options.isRounded()).isTrue();
        assertThat(options.backdropDistance()).isEqualTo(0.0);
        assertThat(options.offset()).isEqualTo(Vector.$(10, 2));
    }
}
