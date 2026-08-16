package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
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
        Time t = Time.now();
        environment.bake(StaticColliderComponent.class, ColliderComponent.class);
        environment.bake(StaticOccluderComponent.class, OccluderComponent.class);
        System.out.println("BAKED " + Duration.since(t).milliseconds());
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