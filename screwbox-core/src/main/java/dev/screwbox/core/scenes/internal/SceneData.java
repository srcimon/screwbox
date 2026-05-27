package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.scenes.Scene;

class SceneData {
    private final Scene scene;
    private final DefaultEnvironment environment;
    private boolean isInitialized;

    SceneData(final Scene scene, final DefaultEnvironment environment) {
        this.scene = scene;
        this.environment = environment;
    }

    void initialize() {
        scene.populate(environment);
        isInitialized = true;
    }

    DefaultEnvironment environment() {
        return environment;
    }

    boolean isSameAs(final Class<? extends Scene> sceneClass) {
        return scene.getClass().equals(sceneClass);
    }

    Scene scene() {
        return scene;
    }

    boolean isInitialized() {
        return isInitialized;
    }
}