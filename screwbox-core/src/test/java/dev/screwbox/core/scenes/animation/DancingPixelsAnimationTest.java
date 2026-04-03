package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.DancingPixelsAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DancingPixelsAnimationTest extends AnimationTest {

    @Test
    void newInstance_invalidSize_throwsException() {
        Size invalidSize = Size.of(0, 1);
        assertThatThrownBy(() -> new DancingPixelsAnimation(invalidSize, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be valid");
    }

    @Test
    void testDancingPixelsAnimationInsideOut() {
        var animation = new DancingPixelsAnimation(Size.of(8, 4), false);
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.5), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testDancingPixelsAnimationInsideOut.png");
    }

    @Test
    void testDancingPixelsAnimationOutsideIn() {
        var animation = new DancingPixelsAnimation(Size.of(8, 4), true);
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.5), 1.0));


        TestUtil.verifyIsSameImage(targetImage, "animations/testDancingPixelsAnimationOutsideIn.png");
    }

}
