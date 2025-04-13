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
        setInitialized();
    }

    public DefaultEnvironment environment() {
        return environment;
    }

    public boolean isSameAs(Class<? extends Scene> sceneClass) {
        return scene.getClass().equals(sceneClass);
    }

    public Scene scene() {
        return scene;
    }

    public void setInitialized() {
        isInitialized = true;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}