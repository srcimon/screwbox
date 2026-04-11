package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CameraLensAnimationTest extends AnimationTest {

    @Test
    void newInstance_ringCountOutOfRange_throwsException() {
        assertThatThrownBy(() -> new CameraLensAnimation(50))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ring count must be in range 4 to 20 (actual value: 50)");
    }

    @Test
    void testCameraLensAnimation() {
        var animation = new CameraLensAnimation();
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.8), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testCameraLensAnimation.png");
    }
}
