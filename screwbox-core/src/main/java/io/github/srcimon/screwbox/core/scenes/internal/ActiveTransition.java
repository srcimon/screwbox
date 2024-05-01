package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
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
        return transition.extroDuration().addTo(started);
    }

    //TODO Time.progressInRange(from, to)
    public Percent introProgress(final Time time) {
        final Time introEndTime = transition.introDuration().addTo(started);
        return Percent.of(time.nanos() / (introEndTime.nanos() - started.nanos()));
    }
}
