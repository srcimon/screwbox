package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

public class ActiveTransition {

    private final Time started = Time.now();
    private final SceneTransition transition;
    private final Class<? extends Scene> targetScene;

    public ActiveTransition(final Class<? extends Scene> targetScene, final SceneTransition transition) {
        this.transition = transition;
        this.targetScene = targetScene;
    }

    public Class<? extends Scene> targetScene() {
        return targetScene;
    }

    public Time switchTime() {
        return transition.outroDuration().addTo(started);
    }

    public Percent introProgress(final Time time) {//TODO Duration.progress()
        var elapsedDuration = Duration.between(time, transition.outroDuration().addTo(started));
        return Percent.of(elapsedDuration.nanos() / (transition.introDuration().nanos() + 1.0));
    }

    public void drawIntro(final Screen screen, final Time time) {//TODO Duration.progress()
        Percent progress = transition.introEase().applyOn(introProgress(time));
        transition.introAnimation().draw(screen, progress);
    }

    public void drawOutro(final Screen screen, final Time time) {//TODO Duration.progress()
        Percent progress = transition.outroEase().applyOn(outroProgress(time));
        transition.outroAnimation().draw(screen, progress);
    }

    public Percent outroProgress(final Time time) {//TODO Duration.progress()
        var elapsedDuration = Duration.between(started, time);
        return Percent.of(elapsedDuration.nanos() / (transition.outroDuration().nanos() + 1.0));
    }
}