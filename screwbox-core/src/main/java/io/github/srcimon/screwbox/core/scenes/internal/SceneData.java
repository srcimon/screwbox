package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.scenes.Scene;

class SceneData {
    private final Scene scene;
    private final DefaultEnvironment environment;
    private boolean isInitialized;
    private Sprite screenshot = null;

    SceneData(final Scene scene, final Engine engine) {
        this.scene = scene;
        this.environment = new DefaultEnvironment(engine);
    }

    void initialize() {
        scene.populate(environment);
        setInitialized();
    }

    public Sprite screenshot() {
        return screenshot;
    }

    public void setScreenshot(final Sprite screenshot) {
        this.screenshot = screenshot;
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