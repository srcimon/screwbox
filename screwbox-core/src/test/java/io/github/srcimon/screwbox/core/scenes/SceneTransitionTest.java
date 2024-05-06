package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SceneTransitionTest {

    @Test
    void custom_introConfigured_createsTransitionWithoutExtroAnimation() {
        var animation = new ColorFadeAnimation();

        var transition = SceneTransition.custom()
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

    @Test
    void custom_extroConfigured_createsTransitionWithoutIntroAnimation() {
        var animation = new ColorFadeAnimation();

        var transition = SceneTransition.custom()
                .extroEase(Ease.SINE_IN)
                .extroDurationSeconds(2)
                .extroAnimation(animation);

        assertThat(transition.extroAnimation()).isEqualTo(animation);
        assertThat(transition.extroEase()).isEqualTo(Ease.SINE_IN);
        assertThat(transition.extroDuration()).isEqualTo(Duration.ofSeconds(2));
        assertThat(transition.introAnimation()).isInstanceOf(ColorFadeAnimation.class);
        assertThat(transition.introDuration()).isEqualTo(Duration.none());
        assertThat(transition.introEase()).isEqualTo(Ease.LINEAR_OUT);
    }
}
