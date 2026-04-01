package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.scenes.animations.GridShredderAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridShredderAnimationTest extends AnimationTest {

    @Test
    void newInstance_invalidSize_throwsException() {
        Size invalidSize = Size.of(0, 1);
        assertThatThrownBy(() -> new GridShredderAnimation(invalidSize))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("grid size must be valid");
    }

    @Test
    @Disabled
    //TODO FIX identical images but different transparent values
    void testGridShredderAnimation() {
        var animation = new GridShredderAnimation(Size.of(2, 4));
        animation.apply(source, target, size, Percent.of(0.2));

        Frame.fromImage(targetImage).exportPng("testGridShredderAnimation.png");
        TestUtil.verifyIsSameImage(targetImage, "animations/testGridShredderAnimation.png");
    }
}
