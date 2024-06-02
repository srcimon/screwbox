package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.Ease.LINEAR_IN;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_OUT;

/**
 * Configures a scene transition. Every transition contains an outro phase (leaving a {@link Scene}) and and intro phase (entering a {@link Scene}).
 *
 * @param outroAnimation animation used for outro
 * @param outroDuration  {@link Duration} of the outro
 * @param outroEase      the {@link Ease} applied on the outro animation
 * @param introAnimation animation used for intro
 * @param introDuration  {@link Duration} of the intro
 * @param introEase      the {@link Ease} applied on the intro animation
 */
public record SceneTransition(
        Animation outroAnimation, Duration outroDuration, Ease outroEase, Animation introAnimation,
        Duration introDuration, Ease introEase
) {

    private static final Animation DEFAULT_ANIMATION = new ColorFadeAnimation();

    /**
     * Switch {@link Scenes} in an instant without any intro or outro {@link Animation}. Can be further customized.
     */
    public static SceneTransition custom() {
        return new SceneTransition(DEFAULT_ANIMATION, Duration.none(), LINEAR_IN, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
    }

    /**
     * Switch {@link Scenes} using an outro {@link Animation}.
     */
    public SceneTransition outroAnimation(final Supplier<Animation> outroAnimation) {
        return outroAnimation(outroAnimation.get());
    }

    /**
     * Switch {@link Scenes} using an outro {@link Animation}.
     */
    public SceneTransition outroAnimation(final Animation outroAnimation) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()} and {@link #outroAnimation()}.
     */
    public SceneTransition ease(final Ease ease) {
        return new SceneTransition(outroAnimation, outroDuration, ease, introAnimation, introDuration, ease);
    }

    /**
     * Sets the {@link Ease} used for {@link #outroAnimation()}.
     */
    public SceneTransition outroEase(final Ease outroEase) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()}.
     */
    public SceneTransition introEase(final Ease introEase) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the intro animation to be played when entering a {@link Scene}.
     */
    public SceneTransition introAnimation(final Supplier<Animation> introAnimation) {
        return introAnimation(introAnimation.get());
    }

    /**
     * Sets the intro animation to be played when entering a {@link Scene}.
     */
    public SceneTransition introAnimation(final Animation introAnimation) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in seconds.
     */
    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in milliseconds.
     */
    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introAnimation, Duration.ofMillis(millis), introEase);
    }

    /**
     * Sets the Duration of the {@link #outroAnimation()} in milliseconds.
     */
    public SceneTransition outroDurationMillis(final long millis) {
        return new SceneTransition(outroAnimation, Duration.ofMillis(millis), outroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #outroAnimation()} in seconds.
     */
    public SceneTransition outroDurationSeconds(final long seconds) {
        return new SceneTransition(outroAnimation, Duration.ofSeconds(seconds), outroEase, introAnimation, introDuration, introEase);
    }
}
