package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlackHoleAnimationTest extends AnimationTest {

    @Test
    void newInstance_invalidCellSize_throwsException() {
        var invalidSize = Size.of(0, 2);

        assertThatThrownBy(() -> new BlackHoleAnimation(invalidSize))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be valid");
    }


    @Test
    void testBlackHoleAnimation() {
        var animation = new BlackHoleAnimation(Size.square(8));
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.6), 1.0));

        Frame.fromImage(targetImage).exportPng("testSpriteFadeAnimation.png");
        TestUtil.verifyIsSameImage(targetImage, "animations/testSpriteFadeAnimation.png");
    }
}
