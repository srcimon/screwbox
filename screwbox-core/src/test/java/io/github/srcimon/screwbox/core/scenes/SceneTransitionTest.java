package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SceneTransitionTest {

    @Test
    void noExtroAnimation_createsTransitionWithoutExtroAnimation() {
        var animation = new ColorFadeAnimation();

        var transition = SceneTransition.noExtroAnimation()
                .introEase(Ease.SINE_IN)
                .introDurationSeconds(2)
                .introAnimation(animation);

        assertThat(transition.introAnimation()).isEqualTo(animation);
        assertThat(transition.introEase()).isEqualTo(Ease.SINE_IN);
        assertThat(transition.introDuration()).isEqualTo(Duration.ofSeconds(2));
        assertThat(transition.extroAnimation()).isInstanceOf(ColorFadeAnimation.class);
        assertThat(transition.extroDuration()).isEqualTo(Duration.none());
        assertThat(transition.extroEase()).isEqualTo(Ease.LINEAR_IN);
    }
}
