package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.WashDownAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WashDownAnimationTest extends AnimationTest {

    @Test
    void newInstance_stripeWidthOutOfRange_throwsException() {
        assertThatThrownBy(() -> new WashDownAnimation(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("stripe width must be in range 1 to 32 (actual value: 0)");
    }

    @Test
    void testWashDownAnimation() {
        var animation = new WashDownAnimation();
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.6), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testWashDownAnimation.png");
    }
}
