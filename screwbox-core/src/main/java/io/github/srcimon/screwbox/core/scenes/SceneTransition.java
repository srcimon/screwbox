package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Duration;

public record SceneTransition(Duration extroDuration) {

    public static SceneTransition noExtro() {
        return extroDuration(Duration.none());
    }

    public static SceneTransition extroDuration(final Duration extroDuration) {
        return new SceneTransition(extroDuration);
    }
}
