package de.suzufa.screwbox.core.scenes.internal;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityEngine;
import de.suzufa.screwbox.core.entityengine.internal.DefaultEntityManager;
import de.suzufa.screwbox.core.entityengine.internal.DefaultSystemManager;
import de.suzufa.screwbox.core.loop.internal.Updatable;
import de.suzufa.screwbox.core.scenes.DefaultScene;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.scenes.Scenes;

public class DefaultScenes implements Scenes, Updatable {

    private final Map<Class<? extends Scene>, SceneContainer> scenes = new HashMap<>();
    private SceneContainer nextActiveScene;
    private SceneContainer activeScene;
    private final Engine engine;

    public DefaultScenes(final Engine engine) {
        this.engine = engine;
        add(new DefaultScene());
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        nextActiveScene = scenes.get(sceneClass);
        return this;
    }

    public DefaultEntityEngine activeEntityEngine() {
        return activeScene.entityEngine();
    }

    @Override
    public Scenes remove(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("scene doesn't exist: " + sceneClass);
        }
        if (activeScene.getClass().equals(sceneClass) || nextActiveScene.getClass().equals(sceneClass)) {
            throw new IllegalArgumentException("cannot remove active scene");
        }
        scenes.remove(sceneClass);
        return this;
    }

    @Override
    public int sceneCount() {
        return scenes.size();
    }

    @Override
    public Scenes add(final Scene scene) {
        final var entityManager = new DefaultEntityManager();
        final var systemManager = new DefaultSystemManager(engine, entityManager);
        final var entityEngine = new DefaultEntityEngine(entityManager, systemManager);

        final SceneContainer sceneContainer = new SceneContainer(scene, entityEngine);
        scene.initialize(sceneContainer.entityEngine());
        scenes.put(scene.getClass(), sceneContainer);

        if (isNull(nextActiveScene)) {
            nextActiveScene = sceneContainer;
            activeScene = sceneContainer;
        }
        return this;
    }

    @Override
    public Scenes add(final Scene... scenes) {
        for (final var scene : scenes) {
            add(scene);
        }
        return this;
    }

    @Override
    public Class<? extends Scene> activeScene() {
        return activeScene.scene().getClass();
    }

    @Override
    public boolean isActive(final Class<? extends Scene> sceneClass) {
        return sceneClass.equals(activeScene());
    }

    @Override
    public EntityEngine entityEngineOf(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        return scenes.get(sceneClass).entityEngine();
    }

    private void ensureSceneExists(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("missing scene: " + sceneClass);
        }
    }

    @Override
    public void update() {
        applySceneChanges();
        activeEntityEngine().update();
    }

    private void applySceneChanges() {
        final boolean sceneChange = !activeScene.equals(nextActiveScene);
        if (sceneChange) {
            activeScene.scene().onExit(engine);
            nextActiveScene.scene().onEnter(engine);
        }
        activeScene = nextActiveScene;
    }

}
