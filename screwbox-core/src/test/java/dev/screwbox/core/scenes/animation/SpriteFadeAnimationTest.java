package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.SpriteFadeAnimation;
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
