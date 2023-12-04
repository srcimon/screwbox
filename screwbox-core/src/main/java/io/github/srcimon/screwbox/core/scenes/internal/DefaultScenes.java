package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.DefaultLoadingScene;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.Scenes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static java.util.Objects.isNull;

public class DefaultScenes implements Scenes, Updatable {

    private class SceneContainer {
        private final Scene scene;
        private final DefaultEnvironment entities;
        boolean isInitialized;

        SceneContainer(Scene scene) {
            this.scene = scene;
            this.entities = new DefaultEnvironment(engine);
        }

        void initialize() {
            scene.populate(entities);
            isInitialized = true;
        }
    }

    private final Map<Class<? extends Scene>, SceneContainer> scenes = new HashMap<>();

    private final Executor executor;
    private final Engine engine;

    private SceneContainer nextActiveScene;
    private SceneContainer activeScene;
    private SceneContainer loadingScene;

    public DefaultScenes(final Engine engine, final Executor executor) {
        this.engine = engine;
        this.executor = executor;
        SceneContainer defaultSceneContainer = new SceneContainer(new DefaultScene());
        defaultSceneContainer.isInitialized = true;
        scenes.put(DefaultScene.class, defaultSceneContainer);
        this.activeScene = defaultSceneContainer;
        this.nextActiveScene = defaultSceneContainer;
        setLoadingScene(new DefaultLoadingScene());
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        nextActiveScene = scenes.get(sceneClass);
        return this;
    }

    public DefaultEnvironment activeEntities() {
        return activeScene.entities;
    }

    @Override
    public Scenes remove(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("scene doesn't exist: " + sceneClass);
        }
        if (activeScene.scene.getClass().equals(sceneClass) || nextActiveScene.scene.getClass().equals(sceneClass)) {
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
    public Scenes addOrReplace(final Scene scene) {
        final var sceneClass = scene.getClass();
        if (contains(sceneClass)) {
            remove(sceneClass);
        }
        add(scene);
        return this;
    }

    @Override
    public boolean contains(Class<? extends Scene> sceneClass) {
        return scenes.containsKey(sceneClass);
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
        return activeScene.scene.getClass();
    }

    @Override
    public boolean isActive(final Class<? extends Scene> sceneClass) {
        return sceneClass.equals(activeScene());
    }

    @Override
    public Environment environmentOf(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        return scenes.get(sceneClass).entities;
    }

    @Override
    public Scenes setLoadingScene(Scene loadingScene) {
        this.loadingScene = new SceneContainer(loadingScene);
        this.loadingScene.initialize();
        return this;
    }

    public boolean isShowingLoading() {
        return !engine.isWarmedUp() || !activeScene.isInitialized;
    }

    @Override
    public void update() {
        applySceneChanges();

        final var sceneToUpdate = isShowingLoading() ? loadingScene : activeScene;
        sceneToUpdate.entities.update();
    }

    private void applySceneChanges() {
        final boolean sceneChange = !activeScene.equals(nextActiveScene);
        if (sceneChange) {
            activeScene.scene.onExit(engine);
            activeScene = nextActiveScene;
            nextActiveScene.scene.onEnter(engine);
        }
    }

    private Scenes add(final Scene scene) {
        final SceneContainer sceneContainer = new SceneContainer(scene);
        executor.execute(sceneContainer::initialize);
        scenes.put(scene.getClass(), sceneContainer);

        if (isNull(nextActiveScene)) {
            nextActiveScene = sceneContainer;
            activeScene = sceneContainer;
        }
        return this;
    }

    private void ensureSceneExists(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("missing scene: " + sceneClass);
        }
    }

}
