package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;

import static io.github.srcimon.screwbox.core.Ease.LINEAR_IN;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_OUT;

public record SceneTransition(
        IntroAnimation introAnimation, Duration introDuration, Ease introEase,
        ExtroAnimation extroAnimation, Duration extroDuration, Ease extroEase
) {

    //TODO validations


    //TODO Export interfaces to scene
    private static final ExtroAnimation NO_EXTRO = (screen, progress) -> {

    };

    private static final IntroAnimation NO_INTRO = (screen, progress, screenshot) -> {

    };

    public static SceneTransition instant() {
        return new SceneTransition(NO_INTRO, Duration.none(), LINEAR_OUT, NO_EXTRO, Duration.none(), LINEAR_IN);
    }

    //TODO javadoc for readability
    public static SceneTransition noExtro() {
        return instant();
    }

    //TODO: split duration and animation possible?
    public static SceneTransition extro(final ExtroAnimation animation, final Duration duration) {
        return new SceneTransition(NO_INTRO, Duration.none(), LINEAR_OUT, animation, duration, LINEAR_IN);
    }

    public SceneTransition intro(final IntroAnimation animation, final Duration duration) {
        return new SceneTransition(animation, duration, introEase, extroAnimation, extroDuration, extroEase);
    }

    public SceneTransition extroEase(final Ease ease) {
        return new SceneTransition(introAnimation, introDuration, ease, extroAnimation, extroDuration, LINEAR_IN);
    }

    public SceneTransition introEase(final Ease ease) {
        return new SceneTransition(introAnimation, introDuration, ease, extroAnimation, extroDuration, extroEase);
    }

    public SceneTransition introDuration(final Duration duration) {
        return new SceneTransition(introAnimation, duration, introEase, extroAnimation, extroDuration, extroEase);
    }

    public SceneTransition extroDuration(final Duration duration) {
        return new SceneTransition(introAnimation, introDuration, introEase, extroAnimation, duration, extroEase);
    }
}
