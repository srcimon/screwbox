package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;

import static io.github.srcimon.screwbox.core.Duration.oneSecond;
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
        ExtroAnimation extroAnimation, Duration extroDuration, Ease extroEase, IntroAnimation introAnimation,
        Duration introDuration, Ease introEase
) {

    //TODO Support incomming graphics as well scene.screenshotfrom(scene.class)
    //TODO validations

    private static final ExtroAnimation NO_EXTRO = (screen, progress) -> {

    };

    private static final IntroAnimation NO_INTRO = (screen, progress, screenshot) -> {

    };

    public static SceneTransition instant() {
        return new SceneTransition(NO_EXTRO, Duration.none(), LINEAR_IN, NO_INTRO, Duration.none(), LINEAR_OUT);
    }

    //TODO javadoc for readability
    public static SceneTransition noExtro() {
        return instant();
    }
    //TODO: split duration and animation possible?
    public static SceneTransition extro(final ExtroAnimation animation, final Duration duration) {
        return new SceneTransition(animation, duration, LINEAR_IN, NO_INTRO, Duration.none(), LINEAR_OUT);
    }

    public SceneTransition extroEase(final Ease ease) {
        return new SceneTransition(extroAnimation, extroDuration, LINEAR_IN, introAnimation, introDuration, ease);
    }

    public SceneTransition introEase(final Ease ease) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, ease);
    }

    public SceneTransition introAnimation(final IntroAnimation introAnimation) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(extroAnimation, extroDuration, extroEase, introAnimation, Duration.ofMillis(millis), introEase);
    }

    public SceneTransition extroDuration(final Duration duration) {
        return new SceneTransition(extroAnimation, duration, extroEase, introAnimation, introDuration, introEase);
    }
}
