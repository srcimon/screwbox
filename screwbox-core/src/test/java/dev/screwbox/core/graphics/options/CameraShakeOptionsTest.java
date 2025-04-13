package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CameraShakeOptionsTest {

    @Test
    void infinite_buildsOptionsWithoutDuration() {
        var options = CameraShakeOptions.infinite()
                .strength(30)
                .interval(ofMillis(10));

        assertThat(options.duration().isNone()).isTrue();
        assertThat(options.xStrength()).isEqualTo(30);
        assertThat(options.yStrength()).isEqualTo(30);
        assertThat(options.interval()).isEqualTo(ofMillis(10));
        assertThat(options.swing()).isEqualTo(Rotation.none());
        assertThat(options.ease()).isEqualTo(Ease.LINEAR_OUT);
    }

    @Test
    void lastingForDuration_buildsOptionsWithDuration() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2)).xStrength(20).yStrength(5);

        assertThat(options.duration()).isEqualTo(ofSeconds(2));
        assertThat(options.xStrength()).isEqualTo(20);
        assertThat(options.yStrength()).isEqualTo(5);
        assertThat(options.interval()).isEqualTo(ofMillis(50));
        assertThat(options.swing()).isEqualTo(Rotation.none());
        assertThat(options.ease()).isEqualTo(Ease.LINEAR_OUT);
    }

    @Test
    void xStrength_xStrengthNegative_throwsException() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThatThrownBy(() -> options.xStrength(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("strength must be positive");
    }

    @Test
    void yStrength_yStrengthNegative_throwsException() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThatThrownBy(() -> options.yStrength(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("strength must be positive");
    }

    @Test
    void screenRotation_rotationNotNull_setsSwing() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2))
                .swing(Rotation.degrees(40));

        assertThat(options.swing()).isEqualTo(Rotation.degrees(40));
    }

    @Test
    void ease_easeNotNull_setsEase() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2))
                .ease(Ease.SIN_IN_OUT_TWICE);

        assertThat(options.ease()).isEqualTo(Ease.SIN_IN_OUT_TWICE);
    }
}
