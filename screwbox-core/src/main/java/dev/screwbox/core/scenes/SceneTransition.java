package dev.screwbox.core.scenes;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.scenes.transitions.ColorFadeTransition;
import dev.screwbox.core.scenes.transitions.TransitionAnimation;

import static dev.screwbox.core.Ease.LINEAR_IN;
import static dev.screwbox.core.Ease.LINEAR_OUT;

/**
 * Configures a scene transition. Every transition is split into an outro phase (leaving a {@link Scene}) and and intro phase
 * (entering the new {@link Scene}).
 *
 * @param outroAnimation animation used for outro
 * @param outroDuration  {@link Duration} of the outro
 * @param outroEase      the {@link Ease} applied on the outro animation
 * @param introAnimation animation used for intro
 * @param introDuration  {@link Duration} of the intro
 * @param introEase      the {@link Ease} applied on the intro animation
 */
public record SceneTransition(
    TransitionAnimation outroAnimation, Duration outroDuration, Ease outroEase,
    TransitionAnimation introAnimation, Duration introDuration, Ease introEase) {

    private static final TransitionAnimation DEFAULT_ANIMATION = new ColorFadeTransition();

    /**
     * Switch {@link Scenes} in an instant without any intro or outro {@link TransitionAnimation}. Can be further customized.
     */
    public static SceneTransition custom() {
        return new SceneTransition(DEFAULT_ANIMATION, Duration.none(), LINEAR_IN, DEFAULT_ANIMATION, Duration.none(), LINEAR_OUT);
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

    /**
     * Sets the {@link #introAnimation()}.
     */
    public SceneTransition introAnimation(final TransitionAnimation introFilter) {
        return new SceneTransition(outroAnimation, outroDuration, outroEase, introFilter, introDuration, introEase);
    }

    /**
     * Sets the {@link #outroAnimation()}.
     */
    public SceneTransition outroAnimation(final TransitionAnimation outroFilter) {
        return new SceneTransition(outroFilter, outroDuration, outroEase, introAnimation, introDuration, introEase);
    }

    //TODO update scene guide
}
