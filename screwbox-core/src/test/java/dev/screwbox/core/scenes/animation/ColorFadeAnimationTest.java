package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.ColorFadeAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

class ColorFadeAnimationTest extends AnimationTest {

    @Test
    void testColorFadeAnimation() {
        var animation = new ColorFadeAnimation(Color.RED);
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.6), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testColorFadeAnimation.png");
    }
}
