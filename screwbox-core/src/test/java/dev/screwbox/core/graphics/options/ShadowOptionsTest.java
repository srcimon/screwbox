package dev.screwbox.core.graphics.options;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShadowOptionsTest {

    @Test
    void newInstance_angular_setsOptions() {
        var options = ShadowOptions.angular()
            .affectOccluder()
            .backdropDistance(2.3);

        assertThat(options.isRounded()).isFalse();
        assertThat(options.backdropDistance()).isEqualTo(2.3);
        assertThat(options.isAffectOccluder()).isTrue();
    }

    @Test
    void newInstance_rounded_setsOptions() {
        var options = ShadowOptions.rounded();

        assertThat(options.isRounded()).isTrue();
        assertThat(options.backdropDistance()).isEqualTo(1.0);
        assertThat(options.isAffectOccluder()).isFalse();
    }

    @Test
    void backdropDistance_negative_throwsException() {
        var options = ShadowOptions.rounded();
        assertThatThrownBy(() -> options.backdropDistance(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("backdrop distance must be positive (actual value: -1.0)");
    }
}
