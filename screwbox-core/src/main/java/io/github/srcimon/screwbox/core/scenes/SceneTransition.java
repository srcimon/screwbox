package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;

public record SceneTransition(
        Duration introDuration,
        IntroAnimation introAnimation,
        Duration extroDuration,
        ExtroAnimation extroAnimation) {

    //TODO validations

    public interface ExtroAnimation {
        void draw(Screen screen, Percent progress);
    }

    public interface IntroAnimation {
        void draw(Screen screen, Percent progress, Sprite previousSceneScreenshot);
    }

    private static final ExtroAnimation NO_EXTRO = (screen, progress) -> {

    };

    private static final IntroAnimation NO_INTRO = (screen, progress, previousSceneScreenshot) -> {

    };

    public static SceneTransition instant() {
        return new SceneTransition(Duration.none(), NO_INTRO, Duration.none(), NO_EXTRO);
    }

    //TODO javadoc for readability
    public static SceneTransition noExtro() {
        return instant();
    }

    public static SceneTransition extro(final ExtroAnimation animation, final Duration duration) {
        return new SceneTransition(Duration.none(), NO_INTRO, duration, animation);
    }

    public SceneTransition intro(final IntroAnimation animation, final Duration duration) {
        return new SceneTransition(duration, animation, extroDuration, extroAnimation);
    }

}
