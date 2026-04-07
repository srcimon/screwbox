package dev.screwbox.core.scenes.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.graphics.internal.DefaultPostProcessing;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.scenes.DefaultLoadingScene;
import dev.screwbox.core.scenes.DefaultScene;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.Scenes;
import dev.screwbox.core.utils.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class DefaultScenes implements Scenes, Updatable {

    private final Map<Class<? extends Scene>, SceneData> sceneData = new HashMap<>();
    private final Executor executor;
    private final Engine engine;
    private final DefaultPostProcessing postProcessing;

    private SceneData activeScene;
    private SceneData loadingScene;
    private ActiveTransition activeTransition;
    private boolean hasChangedToTargetScene = true;
    private SceneTransition defaultTransition = SceneTransition.custom();
    private boolean canRenderTransition = false;
    private Time switchTime = Time.now();

    public DefaultScenes(final Engine engine, final Executor executor, final DefaultPostProcessing postProcessing) {
        this.engine = engine;
        this.executor = executor;
        final SceneData defaultSceneData = createSceneData(new DefaultScene());
        defaultSceneData.setInitialized();
        sceneData.put(DefaultScene.class, defaultSceneData);
        this.activeScene = defaultSceneData;
        setLoadingScene(new DefaultLoadingScene());
        this.postProcessing = postProcessing;
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass) {
        return switchTo(sceneClass, defaultTransition);
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass, final SceneTransition transition) {
        ensureSceneExists(sceneClass);
        activeTransition = new ActiveTransition(Time.now(), sceneClass, transition);
        hasChangedToTargetScene = false;
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
        ensureSceneExists(sceneClass);
        Validate.isFalse(() -> activeScene.isSameAs(sceneClass), "cannot remove active scene");
        sceneData.remove(sceneClass);
        return this;
    }

    @Override
    public int sceneCount() {
        return sceneData.size();
    }

    @Override
    public Scenes resetActiveScene() {
        return resetActiveScene(defaultTransition);
    }

    @Override
    public Scenes resetActiveScene(final SceneTransition transition) {
        final Scene sceneToReplace = activeScene.scene();
        addOrReplace(sceneToReplace);
        switchTo(sceneToReplace.getClass(), transition);
        return this;
    }

    @Override
    public Scenes setDefaultTransition(final SceneTransition transition) {
        this.defaultTransition = requireNonNull(transition, "transition must not be null");
        return this;
    }

    @Override
    public Scenes addOrReplace(final Scene scene) {
        final var sceneClass = scene.getClass();
        if (exists(sceneClass)) {
            sceneData.remove(sceneClass);
        }
        add(scene);
        return this;
    }

    @Override
    public boolean exists(Class<? extends Scene> sceneClass) {
        return sceneData.containsKey(sceneClass);
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
        return sceneData.get(sceneClass).environment();
    }

    @Override
    public Scenes setLoadingScene(final Scene loadingScene) {
        this.loadingScene = createSceneData(loadingScene);
        this.loadingScene.initialize();
        return this;
    }

    @Override
    public Time switchTime() {
        return switchTime;
    }

    public boolean isShowingLoadingScene() {
        return !activeScene.isInitialized();
    }

    @Override
    public void update() {
        final Time time = Time.now();
        final var sceneToUpdate = isShowingLoadingScene() ? loadingScene : activeScene;
        sceneToUpdate.environment().update();

        if (isTransitioning()) {
            canRenderTransition = true;
            final boolean mustSwitchScenes = !hasChangedToTargetScene && time.isAfter(activeTransition.switchTime());
            if (mustSwitchScenes) {
                activeScene.scene().onExit(engine);
                activeScene = sceneData.get(activeTransition.targetScene());
                activeScene.scene().onEnter(engine);
                hasChangedToTargetScene = true;
                switchTime = time;
            }
            if (hasChangedToTargetScene && activeTransition.introProgress(time).isMax()) {
                activeTransition = null;
                canRenderTransition = false;
            }
        }

        if (canRenderTransition) {
            if (!isShowingLoadingScene() && hasChangedToTargetScene) {
                postProcessing.setTransitionFilter(activeTransition.introFilter(time));
            } else {
                postProcessing.setTransitionFilter(activeTransition.outroFilter(time));
            }
        } else {
            postProcessing.setTransitionFilter(null);
        }

    }

    private void add(final Scene scene) {
        final var sceneClass = scene.getClass();
        Validate.isFalse(() -> sceneData.containsKey(sceneClass), "scene is already present: " + sceneClass);
        final SceneData data = createSceneData(scene);
        executor.execute(data::initialize);
        this.sceneData.put(sceneClass, data);
    }

    private void ensureSceneExists(final Class<? extends Scene> sceneClass) {
        if (!exists(sceneClass)) {
            throw new IllegalArgumentException("scene doesn't exist: " + sceneClass);
        }
    }

    private SceneData createSceneData(final Scene scene) {
        final var sceneEnvironment = new DefaultEnvironment(engine);
        return new SceneData(scene, sceneEnvironment);
    }
}
