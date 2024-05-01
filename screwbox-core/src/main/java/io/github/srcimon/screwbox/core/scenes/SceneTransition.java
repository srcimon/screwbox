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

    public static SceneTransition instant() {
        return new SceneTransition(Duration.none(), null, Duration.none(), null);
    }

    public static SceneTransition introOnly(IntroAnimation introAnimation, Duration duration) {
        return new SceneTransition(duration, introAnimation, Duration.none(), null);
    }

    public static SceneTransition extroOnly(final ExtroAnimation extroAnimation, final Duration duration) {
        return new SceneTransition(Duration.none(), null, duration, extroAnimation);
    }

}
