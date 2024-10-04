package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
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
    }

    @Test
    void lastingForDuration_buildsOptionsWithDuration() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2)).xStrength(20).yStrength(5);

        assertThat(options.duration()).isEqualTo(ofSeconds(2));
        assertThat(options.xStrength()).isEqualTo(20);
        assertThat(options.yStrength()).isEqualTo(5);
        assertThat(options.interval()).isEqualTo(ofMillis(50));
    }

    @Test
    void strength_xStrengthNegative_throwsException() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThatThrownBy(() -> options.xStrength(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("strength must be positive");
    }

    @Test
    void strength_yStrengthNegative_throwsException() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThatThrownBy(() -> options.yStrength(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("strength must be positive");
    }
}
