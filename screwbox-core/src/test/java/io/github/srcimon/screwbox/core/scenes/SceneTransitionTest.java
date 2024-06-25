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
        assertThat(transition.outroAnimation()).isInstanceOf(ColorFadeAnimation.class);
        assertThat(transition.outroDuration()).isEqualTo(Duration.none());
        assertThat(transition.outroEase()).isEqualTo(Ease.LINEAR_IN);
    }

    @Test
    void custom_extroConfigured_createsTransitionWithoutIntroAnimation() {
        var animation = new ColorFadeAnimation();

        var transition = SceneTransition.custom()
                .outroEase(Ease.SINE_IN)
                .outroDurationSeconds(2)
                .outroAnimation(animation);

        assertThat(transition.outroAnimation()).isEqualTo(animation);
        assertThat(transition.outroEase()).isEqualTo(Ease.SINE_IN);
        assertThat(transition.outroDuration()).isEqualTo(Duration.ofSeconds(2));
        assertThat(transition.introAnimation()).isInstanceOf(ColorFadeAnimation.class);
        assertThat(transition.introDuration()).isEqualTo(Duration.none());
        assertThat(transition.introEase()).isEqualTo(Ease.LINEAR_OUT);
    }

    @Test
    void custom_introAndOutroConfigured_createsTransition() {
        var animation = new ColorFadeAnimation();

        var transition = SceneTransition.custom()
                .outroEase(Ease.LINEAR_IN)
                .outroDurationSeconds(2)
                .outroAnimation(animation)
                .introEase(Ease.SINE_IN)
                .introDurationSeconds(4)
                .introAnimation(animation);

        assertThat(transition.introAnimation()).isEqualTo(animation);
        assertThat(transition.introEase()).isEqualTo(Ease.SINE_IN);
        assertThat(transition.introDuration()).isEqualTo(Duration.ofSeconds(4));
        assertThat(transition.outroAnimation()).isInstanceOf(ColorFadeAnimation.class);
        assertThat(transition.outroDuration()).isEqualTo(Duration.ofSeconds(2));
        assertThat(transition.outroEase()).isEqualTo(Ease.LINEAR_IN);
    }
}
