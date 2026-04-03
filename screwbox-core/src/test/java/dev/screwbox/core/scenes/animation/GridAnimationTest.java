package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.GridAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

class GridAnimationTest extends AnimationTest {

    @Test
    void testGridAnimation() {
        var animation = new GridAnimation(Size.of(3, 2));
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.1), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testGridAnimation.png");
    }

}
