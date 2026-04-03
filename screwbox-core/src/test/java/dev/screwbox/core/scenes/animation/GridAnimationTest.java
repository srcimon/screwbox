package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.GridAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridAnimationTest extends AnimationTest {

    @Test
    void newInstance_invalidSize_throwsException() {
        Size invalidSize = Size.of(0, 1);
        assertThatThrownBy(() -> new GridAnimation(invalidSize))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be valid");
    }

    @Test
    void testGridAnimation() {
        var animation = new GridAnimation(Size.of(8, 8));
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.1), 1.0));

        TestUtil.verifyIsSameImage(targetImage, "animations/testGridAnimation.png");
    }

}
