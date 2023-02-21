package io.github.simonbas.screwbox.core.scenes.internal;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.internal.DefaultEntities;
import io.github.simonbas.screwbox.core.loop.internal.Updatable;
import io.github.simonbas.screwbox.core.scenes.DefaultScene;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.scenes.Scenes;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class DefaultScenes implements Scenes, Updatable {

    private record SceneContainer(Scene scene, DefaultEntities entities) {

    }

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

    public DefaultEntities activeEntities() {
        return activeScene.entities();
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
        final var entities = new DefaultEntities(engine);

        final SceneContainer sceneContainer = new SceneContainer(scene, entities);
        scene.initialize(sceneContainer.entities());
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
    public Entities entitiesOf(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        return scenes.get(sceneClass).entities();
    }

    private void ensureSceneExists(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("missing scene: " + sceneClass);
        }
    }

    @Override
    public void update() {
        applySceneChanges();
        activeEntities().update();
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
