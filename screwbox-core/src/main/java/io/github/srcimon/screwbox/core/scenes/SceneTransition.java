package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.scenes.animations.ColorFadeAnimation;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.Ease.LINEAR_IN;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_OUT;

/**
 * Configures a scene transition. Every transition contains an extro phase (leaving a {@link Scene}) and and intro phase (entering a {@link Scene}).
 *
 * @param extroAnimation animation used for extro
 * @param extroDuration  {@link Duration} of the extro
 * @param extroEase      the {@link Ease} applied on the extro animation
 * @param introAnimation animation used for intro
 * @param introDuration  {@link Duration} of the intro
 * @param introEase      the {@link Ease} applied on the intro animation
 */
public record SceneTransition(
        Animation extroAnimation, Duration extroDuration, Ease extroEase, Animation introAnimation,
        Duration introDuration, Ease introEase
) {

    private static final Animation DEFAULT_ANIMATION = new ColorFadeAnimation();

    /**
     * Switch {@link Scenes} in an instant without any intro or extro {@link Animation}. Can be further customized.
     */
    public static SceneTransition custom() {
        return new SceneTransition(DEFAULT_ANIMATION, Duration.none(), LINEAR_IN, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
    }

    /**
     * Switch {@link Scenes} using an extro {@link Animation}.
     */
    public SceneTransition extroAnimation(final Supplier<Animation> extroAnimation) {
        return extroAnimation(extroAnimation.get());
    }

    /**
     * Switch {@link Scenes} using an extro {@link Animation}.
     */
    public SceneTransition extroAnimation(final Animation extroAnimation) {
        return new SceneTransition(extroAnimation, Duration.none(), LINEAR_IN, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()} and {@link #extroAnimation()}.
     */
    public SceneTransition ease(final Ease ease) {
        return new SceneTransition(extroAnimation, extroDuration, ease, introAnimation, introDuration, ease);
    }

    /**
     * Sets the {@link Ease} used for {@link #extroAnimation()}.
     */
    public SceneTransition extroEase(final Ease extroEase) {
        return new SceneTransition(extroAnimation, extroDuration, LINEAR_IN, introAnimation, introDuration, extroEase);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()}.
     */
    public SceneTransition introEase(final Ease introEase) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, introEase);
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
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in seconds.
     */
    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in milliseconds.
     */
    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofMillis(millis), introEase);
    }

    /**
     * Sets the Duration of the {@link #extroAnimation()} in milliseconds.
     */
    public SceneTransition extroDurationMillis(final long millis) {
        return new SceneTransition(extroAnimation, Duration.ofMillis(millis), extroEase, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #extroAnimation()} in seconds.
     */
    public SceneTransition extroDurationSeconds(final long seconds) {
        return new SceneTransition(extroAnimation, Duration.ofSeconds(seconds), extroEase, introAnimation, introDuration, introEase);
    }
}
