package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;

class CameraShakeOptionsTest {

    @Test
    void testBuildingOfOptionsWithoutDuration() {
        var options = CameraShakeOptions.infinite()
                .strength(30)
                .interval(ofMillis(10));

        assertThat(options.duration().isNone()).isTrue();
        assertThat(options.strength()).isEqualTo(30);
        assertThat(options.interval()).isEqualTo(ofMillis(10));
    }
}
