package dev.screwbox.core.scenes;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
import dev.screwbox.core.scenes.animations.ColorFadeAnimation;
import dev.screwbox.core.scenes.animations.TransitionPostFilter;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.screwbox.core.Ease.LINEAR_IN;
import static dev.screwbox.core.Ease.LINEAR_OUT;

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
    TransitionPostFilter outroFilter, Animation outroAnimation, Duration outroDuration,
    Ease outroEase,
    TransitionPostFilter introFilter, Animation introAnimation, Duration introDuration,
    Ease introEase) {

    private static final Animation DEFAULT_ANIMATION = new ColorFadeAnimation();

    /**
     * Switch {@link Scenes} in an instant without any intro or outro {@link Animation}. Can be further customized.
     */
    public static SceneTransition custom() {
        return new SceneTransition(null, DEFAULT_ANIMATION, Duration.none(), LINEAR_IN, null, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
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
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, LINEAR_OUT);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()} and {@link #outroAnimation()}.
     */
    public SceneTransition ease(final Ease ease) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, ease, introFilter, introAnimation, introDuration, ease);
    }

    /**
     * Sets the {@link Ease} used for {@link #outroAnimation()}.
     */
    public SceneTransition outroEase(final Ease outroEase) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the {@link Ease} used for {@link #introAnimation()}.
     */
    public SceneTransition introEase(final Ease introEase) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, introEase);
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
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in seconds.
     */
    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    /**
     * Sets the Duration of the {@link #introAnimation()} in milliseconds.
     */
    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, Duration.ofMillis(millis), introEase);
    }

    /**
     * Sets the Duration of the {@link #outroAnimation()} in milliseconds.
     */
    public SceneTransition outroDurationMillis(final long millis) {
        return new SceneTransition(outroFilter, outroAnimation, Duration.ofMillis(millis), outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    /**
     * Sets the Duration of the {@link #outroAnimation()} in seconds.
     */
    public SceneTransition outroDurationSeconds(final long seconds) {
        return new SceneTransition(outroFilter, outroAnimation, Duration.ofSeconds(seconds), outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    public SceneTransition introFilter(final TransitionPostFilter introFilter) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    public SceneTransition outroFilter(final TransitionPostFilter outroFilter) {
        return new SceneTransition(outroFilter, outroAnimation, outroDuration, outroEase, introFilter, introAnimation, introDuration, introEase);
    }

    //TODO removed RenderSceneTransitionSystem changelog
    //TODO update scene guide
}
