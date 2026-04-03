package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.scenes.animations.CameraLensAnimation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CameraLensAnimationTest extends AnimationTest {

    @Test
    void newInstance_ringCountOutOfRange_throwsException() {
        assertThatThrownBy(() -> new CameraLensAnimation(50))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ring count must be in range 4 to 20 (actual value: 50)");
    }
}
