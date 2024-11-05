package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.internal.RenderingSubsystem;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.DefaultLoadingScene;
import io.github.srcimon.screwbox.core.scenes.DefaultScene;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.Scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Order(Order.SystemOrder.SCENE_TRANSITIONS)
public class DefaultScenes implements Scenes, Updatable, RenderingSubsystem {

    private final Map<Class<? extends Scene>, SceneData> sceneData = new HashMap<>();
    private final List<RenderingSubsystem> renderingSubsystems = new ArrayList<>();
    private final Executor executor;
    private final Engine engine;
    private final Canvas canvas;

    private SceneData activeScene;
    private SceneData loadingScene;
    private ActiveTransition activeTransition;
    private boolean hasChangedToTargetScene = true;
    private SceneTransition defaultTransition = SceneTransition.custom();

    public DefaultScenes(final Engine engine, final Canvas canvas, final Executor executor) {
        this.engine = engine;
        this.executor = executor;
        this.canvas = canvas;
        SceneData defaultSceneData = createSceneData(new DefaultScene());
        defaultSceneData.setInitialized();
        sceneData.put(DefaultScene.class, defaultSceneData);
        this.activeScene = defaultSceneData;
        setLoadingScene(new DefaultLoadingScene());
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass) {
        return switchTo(sceneClass, defaultTransition);
    }

    @Override
    public Scenes switchTo(final Class<? extends Scene> sceneClass, final SceneTransition transition) {
        ensureSceneExists(sceneClass);
        activeTransition = new ActiveTransition(sceneClass, transition);
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
        if (activeScene.isSameAs(sceneClass)) {
            throw new IllegalArgumentException("cannot remove active scene");
        }
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

    public boolean isShowingLoadingScene() {
        return !engine.isWarmedUp() || !activeScene.isInitialized();
    }

    @Override
    public void update() {
        final var sceneToUpdate = isShowingLoadingScene() ? loadingScene : activeScene;
        sceneToUpdate.environment().update();

        if (isTransitioning()) {
            final Time time = Time.now();
            final boolean mustSwitchScenes = !hasChangedToTargetScene && time.isAfter(activeTransition.switchTime());
            if (mustSwitchScenes) {
                activeScene.scene().onExit(engine);
                activeScene = sceneData.get(activeTransition.targetScene());
                activeScene.scene().onEnter(engine);
                hasChangedToTargetScene = true;
            }
            if (hasChangedToTargetScene && activeTransition.introProgress(time).isMax()) {
                activeTransition = null;
            }
        }
    }

    @Override
    public void render() {
        if (isTransitioning()) {
            if (!isShowingLoadingScene() && hasChangedToTargetScene) {
                activeTransition.drawIntro(canvas, Time.now());
            } else {
                activeTransition.drawOutro(canvas, Time.now());
            }
        }
    }

    private void add(final Scene scene) {
        final var sceneClass = scene.getClass();
        if (sceneData.containsKey(sceneClass)) {
            throw new IllegalArgumentException("scene is already present: " + sceneClass);
        }
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
        final DefaultEnvironment sceneEnvironment = new DefaultEnvironment(engine);
        for (final var renderingSubsystem : renderingSubsystems) {
            final var order = renderingSubsystem.getClass().getAnnotation(Order.class).value();
            sceneEnvironment.addSystem(order, engine -> renderingSubsystem.render());        //TODO FIX : virtual systems can be seen
        }
        return new SceneData(scene, sceneEnvironment);
    }

    public void addRenderingSubsystems(final List<RenderingSubsystem> renderingSubsystems) {
        this.renderingSubsystems.addAll(renderingSubsystems);
    }
}
