package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Screen;

//TODO just prototyping / rename everything
public record SceneTransition(TransitionAnimation in, Duration inDuration, TransitionAnimation out, Duration outDuration) {

    public static final TransitionAnimation NO_ANMIATION = (screen, progress) -> {
    };

    @FunctionalInterface
    public interface TransitionAnimation {
        void draw(Screen screen, Percent progress);
    }

    public static SceneTransition in(TransitionAnimation in) {
        return new SceneTransition(in, Duration.ofMillis(1500), NO_ANMIATION, Duration.none());
    }

    public static SceneTransition out(TransitionAnimation out) {
        return new SceneTransition(NO_ANMIATION, Duration.none(), out, Duration.ofMillis(1500));
    }

    public Duration duration() {
        return inDuration().add(outDuration());
    }
}
