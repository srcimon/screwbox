package io.github.srcimon.screwbox.core.graphics;

import org.assertj.core.api.Assertions;
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
        assertThat(options.strength()).isEqualTo(30);
        assertThat(options.interval()).isEqualTo(ofMillis(10));
    }

    @Test
    void lastingForDuration_buildsOptionsWithDuration() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThat(options.duration()).isEqualTo(ofSeconds(2));
        assertThat(options.strength()).isEqualTo(10);
        assertThat(options.interval()).isEqualTo(ofMillis(50));
    }

    @Test
    void strength_zeroStrength_throwsException() {
        var options = CameraShakeOptions.lastingForDuration(ofSeconds(2));

        assertThatThrownBy(() -> options.strength(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("strength must be positive");
    }
}
