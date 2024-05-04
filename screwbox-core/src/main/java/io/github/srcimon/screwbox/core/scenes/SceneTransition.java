package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.Ease.LINEAR_IN;
import static io.github.srcimon.screwbox.core.Ease.LINEAR_OUT;

/**
 * Configures a scene transition. Every transition contains an extro phase (leaving a {@link Scene}) and and intro phase (entering a {@link Scene}).
 *
 * @param animation animation used for extro
 * @param extroDuration  {@link Duration} of the extro
 * @param extroEase      the {@link Ease} applied on the extro animation
 * @param introAnimation animation used for intro
 * @param introDuration  {@link Duration} of the intro
 * @param introEase      the {@link Ease} applied on the intro animation
 */
public record SceneTransition(
        Animation animation, Duration extroDuration, Ease extroEase, Animation introAnimation,
        Duration introDuration, Ease introEase
) {

    //TODO Support incomming graphics as well scene.screenshotfrom(scene.class)
    //TODO validations

    private static final Animation NO_ANIMATION = (screen, progress) -> {//TODO: BUNDLE

    };


    //TODO javadoc for readability
    public static SceneTransition instant() {
        return new SceneTransition(NO_ANIMATION, Duration.none(), LINEAR_IN, NO_ANIMATION, Duration.none(), LINEAR_OUT);
    }


    public static SceneTransition noExtroAnimation() {
        return instant();
    }
    //TODO: split duration and animation possible?

    public static SceneTransition extroAnimation(final Supplier<Animation> extroAnimation) {
        return extroAnimation(extroAnimation.get());
    }

    public static SceneTransition extroAnimation(final Animation animation) {
        return new SceneTransition(animation, Duration.none(), LINEAR_IN, NO_ANIMATION, Duration.none(), LINEAR_OUT);
    }

    public SceneTransition ease(final Ease ease) {
        return new SceneTransition(animation, extroDuration, ease, introAnimation, introDuration, ease);
    }

    public SceneTransition extroEase(final Ease extroEase) {
        return new SceneTransition(animation, extroDuration, LINEAR_IN, introAnimation, introDuration, extroEase);
    }

    public SceneTransition introEase(final Ease introEase) {
        return new SceneTransition(animation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition introAnimation(final Supplier<Animation> introAnimation) {
        return introAnimation(introAnimation.get());
    }

    public SceneTransition introAnimation(final Animation introAnimation) {
        return new SceneTransition(animation, extroDuration, extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition introDurationSeconds(final long seconds) {
        return new SceneTransition(animation, extroDuration, extroEase, introAnimation, Duration.ofSeconds(seconds), introEase);
    }

    public SceneTransition introDurationMillis(final long millis) {
        return new SceneTransition(animation, extroDuration, extroEase, introAnimation, Duration.ofMillis(millis), introEase);
    }

    public SceneTransition extroDurationMillis(final long millis) {
        return new SceneTransition(animation, Duration.ofMillis(millis), extroEase, introAnimation, introDuration, introEase);
    }

    public SceneTransition extroDurationSeconds(final long seconds) {
        return new SceneTransition(animation, Duration.ofSeconds(seconds), extroEase, introAnimation, introDuration, introEase);
    }
}
