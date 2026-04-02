package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.scenes.animations.GridAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridAnimationTest extends AnimationTest {

    @Test
    void newInstance_gridSizeOutOfRange_throwsException() {
        assertThatThrownBy(() -> new GridAnimation(10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("grid size must in range 32 to 480 (actual value: 10)");
    }

    @Test
    void testGridAnimation() {
        var animation = new GridAnimation(32);
        animation.apply(source, target, size, Percent.of(0.1));

        TestUtil.verifyIsSameImage(targetImage, "animations/testGridAnimation.png");
    }

}
