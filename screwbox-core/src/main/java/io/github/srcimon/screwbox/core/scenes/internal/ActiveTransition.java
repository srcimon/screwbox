package io.github.srcimon.screwbox.core.scenes.internal;

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

    public boolean isTimeToSwitchScenes(final Time time) {
        return time.isAfter(transition.extroDuration().addTo(started));
    }

    public Class<? extends Scene> targetScene() {
        return targetScene;
    }
}
