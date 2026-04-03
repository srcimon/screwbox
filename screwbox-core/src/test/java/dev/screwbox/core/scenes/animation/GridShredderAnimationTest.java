package dev.screwbox.core.scenes.animation;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.scenes.animations.AnimationContext;
import dev.screwbox.core.scenes.animations.GridShredderAnimation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testGridShredderAnimation() {
        var animation = new GridShredderAnimation(Size.of(2, 4));
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.2), 1.0));

        // no result image pixel perfect check because of rounding errors
        var result = Frame.fromImage(targetImage);
        assertThat(result.colorAt(4, 9).rgb()).isEqualTo(-859713318);
        assertThat(result.colorAt(8, 9).rgb()).isEqualTo(-859713318);
        assertThat(result.colorAt(20, 4).rgb()).isEqualTo(-859713318);
        assertThat(result.colorAt(1, 2).rgb()).isZero();
    }
}
