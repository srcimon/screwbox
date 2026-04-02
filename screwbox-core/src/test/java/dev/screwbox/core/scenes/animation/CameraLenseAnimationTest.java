package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.scenes.animations.CameraLenseAnimation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CameraLenseAnimationTest extends AnimationTest {

    @Test
    void newInstance_ringCountOutOfRange_throwsException() {
        assertThatThrownBy(() -> new CameraLenseAnimation(50))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ring count must be in range 4 to 20 (actual value: 50)");
    }
}
