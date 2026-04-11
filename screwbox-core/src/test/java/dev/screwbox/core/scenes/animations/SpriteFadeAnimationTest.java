package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

class SpriteFadeAnimationTest extends AnimationTest {

    @Test
    void testSpriteFadeAnimation() {
        var animation = new SpriteFadeAnimation(SpriteBundle.DOT_WHITE.get());
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.6), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testSpriteFadeAnimation.png");
    }
}
