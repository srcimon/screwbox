package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.DefaultLoadingScene;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.Scenes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

import static java.util.Objects.nonNull;

public class DefaultScenes implements Scenes, Updatable {

    private final Map<Class<? extends Scene>, SceneContainer> scenes = new HashMap<>();

    private final Executor executor;
    private final Engine engine;
    private final Screen screen;
    private Sprite previousSceneScreenshot;

    private SceneContainer activeScene;
    private SceneContainer loadingScene;
    private ActiveTransition activeTransition;


    public DefaultScenes(final Engine engine, final Screen screen, final Executor executor) {
        this.engine = engine;
        this.executor = executor;
        this.screen = screen;
        SceneContainer defaultSceneContainer = new SceneContainer(new DefaultScene(), engine);
        defaultSceneContainer.isInitialized = true;
        scenes.put(DefaultScene.class, defaultSceneContainer);
        this.activeScene = defaultSceneContainer;
        setLoadingScene(new DefaultLoadingScene());
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass) {
        return switchTo(sceneClass, SceneTransition.instant());
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass, final SceneTransition transition) {
        ensureSceneExists(sceneClass);
        activeTransition = new ActiveTransition(sceneClass, transition);
        return this;
    }

    @Override
    public boolean isTransitioning() {
        return nonNull(activeTransition);
    }

    public DefaultEnvironment activeEnvironment() {
        return activeScene.environment();
    }

    @Override
    public Scenes remove(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("scene doesn't exist: " + sceneClass);
        }
        if (activeScene.sameSceneAs(sceneClass)) {
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
        return activeScene.scene().getClass();
    }

    @Override
    public boolean isActive(final Class<? extends Scene> sceneClass) {
        return sceneClass.equals(activeScene());
    }

    @Override
    public Environment environmentOf(final Class<? extends Scene> sceneClass) {
        ensureSceneExists(sceneClass);
        return scenes.get(sceneClass).environment();
    }

    @Override
    public Scenes setLoadingScene(final Scene loadingScene) {
        this.loadingScene = new SceneContainer(loadingScene, engine);
        this.loadingScene.initialize();
        return this;
    }

    @Override
    public Optional<Sprite> previousSceneScreenshot() {
        return Optional.ofNullable(previousSceneScreenshot);
    }

    public boolean isShowingLoadingScene() {
        return !engine.isWarmedUp() || !activeScene.isInitialized;
    }

    @Override
    public void update() {
        final var sceneToUpdate = isShowingLoadingScene() ? loadingScene : activeScene;
        sceneToUpdate.environment().update();

        if (isTransitioning()) {
            final Time time = Time.now();
            final boolean hasChangedToTargetScene = activeTransition.targetScene().equals(activeScene.scene().getClass());
            final boolean mustSwitchScenes = !hasChangedToTargetScene && time.isAfter(activeTransition.switchTime());
            if (mustSwitchScenes) {
                previousSceneScreenshot = screen.takeScreenshot();
                activeScene.scene().onExit(engine);
                activeScene = scenes.get(activeTransition.targetScene());
                activeScene.scene().onEnter(engine);
            }
            if(hasChangedToTargetScene) {
                activeTransition.transition().introAnimation().draw(engine.graphics().screen(), activeTransition.introProgress(time), previousSceneScreenshot);
            } else {
                activeTransition.transition().extroAnimation().draw(engine.graphics().screen(), activeTransition.extroProgress(time));
            }
            //TODO remove transition
        }

    }

    //TODO ScrenshotOfScene(Scene.class)

    private void add(final Scene scene) {
        final SceneContainer sceneContainer = new SceneContainer(scene, engine);
        executor.execute(sceneContainer::initialize);
        scenes.put(scene.getClass(), sceneContainer);
    }

    private void ensureSceneExists(final Class<? extends Scene> sceneClass) {
        if (!scenes.containsKey(sceneClass)) {
            throw new IllegalArgumentException("missing scene: " + sceneClass);
        }
    }

}
