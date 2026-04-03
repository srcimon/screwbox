package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.CameraLensAnimation;
import dev.screwbox.core.scenes.animations.ColorFadeAnimation;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CameraLensAnimationTest extends AnimationTest {

    @Test
    void newInstance_ringCountOutOfRange_throwsException() {
        assertThatThrownBy(() -> new CameraLensAnimation(50))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ring count must be in range 4 to 20 (actual value: 50)");
    }

    @Test
    void testCameraLensAnimation() {
        var animation = new CameraLensAnimation();
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.8), 1.0));

        // no result image pixel perfect check because of rounding errors
        var result = Frame.fromImage(targetImage);
        assertThat(result.colorAt(4, 9).rgb()).isEqualTo(176200115);
        assertThat(result.colorAt(8, 9).rgb()).isEqualTo(1736351413);
        assertThat(result.colorAt(20, 4).rgb()).isEqualTo(1233035702);
        assertThat(result.colorAt(1, 2).rgb()).isZero();
    }
}
