package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GridShredderAnimationTest extends AnimationTest {

    @Test
    void newInstance_invalidSize_throwsException() {
        Size invalidSize = Size.of(0, 1);
        assertThatThrownBy(() -> new GridShredderAnimation(invalidSize))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("cell size must be valid");
    }

    @Test
    void testGridShredderAnimation() {
        var animation = new GridShredderAnimation(Size.of(20, 20));
        animation.apply(source, target, new AnimationContext(size, Percent.of(0.3), 1.0));

        // no result image pixel perfect check because of rounding errors
        var result = Frame.fromImage(targetImage);
        assertThat(result.colorAt(4, 9).rgb()).isZero();
        assertThat(result.colorAt(8, 9).rgb()).isEqualTo(-1295986984);
        assertThat(result.colorAt(20, 4).rgb()).isEqualTo(-1295986984);
        assertThat(result.colorAt(1, 2).rgb()).isZero();
    }
}
