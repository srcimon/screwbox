package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.TweenMode;

import static io.github.srcimon.screwbox.core.TweenMode.LINEAR_IN;
import static io.github.srcimon.screwbox.core.TweenMode.LINEAR_OUT;

public record SceneTransition(
        IntroAnimation introAnimation, Duration introDuration, TweenMode introTweenMode,
        ExtroAnimation extroAnimation, Duration extroDuration, TweenMode extroTweenMode
) {

    //TODO validations


    //TODO Export interfaces to scene
    private static final ExtroAnimation NO_EXTRO = (screen, progress) -> {

    };

    //TODO Rename tween mode to easing see https://github.com/vydd/easing

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
        return new SceneTransition(animation, duration, introTweenMode, extroAnimation, extroDuration, extroTweenMode);
    }

    public SceneTransition extroTweenMode(final TweenMode tweenMode) {
        return new SceneTransition(introAnimation, introDuration, tweenMode, extroAnimation, extroDuration, LINEAR_IN);
    }

    public SceneTransition introTweenMode(final TweenMode tweenMode) {
        return new SceneTransition(introAnimation, introDuration, tweenMode, extroAnimation, extroDuration, extroTweenMode);
    }

    public SceneTransition introDuration(final Duration duration) {
        return new SceneTransition(introAnimation, duration, introTweenMode, extroAnimation, extroDuration, extroTweenMode);
    }

    public SceneTransition extroDuration(final Duration duration) {
        return new SceneTransition(introAnimation, introDuration, introTweenMode, extroAnimation, duration, extroTweenMode);
    }
}
