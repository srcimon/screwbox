package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

public class ActiveTransition {

    private final Time started = Time.now();
    private final SceneTransition transition;

    public ActiveTransition(final SceneTransition transition) {
        this.transition = transition;
    }
}
