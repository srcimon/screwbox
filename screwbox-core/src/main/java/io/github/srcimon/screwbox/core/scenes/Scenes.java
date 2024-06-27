package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;

import java.util.function.Supplier;

/**
 * Manage different game situations like pause or options in different {@link Scenes}.
 * <p>
 * The {@link Engine} is always started with a {@link DefaultScene} present.
 */
public interface Scenes {

    /**
     * Clears all {@link Environment#entities()} and {@link Environment#systems()} and repopulates the active scene.
     * Uses the default transition to reenter the current scene.
     *
     * @see #resetActiveScene(SceneTransition)
     */
    Scenes resetActiveScene();

    /**
     * Clears all {@link Environment#entities()} and {@link Environment#systems()} and repopulates the active scene.
     * Uses the specified transition to reenter the current scene.
     *
     * @see #resetActiveScene()
     */
    Scenes resetActiveScene(SceneTransition transition);

    /**
     * Sets the default {@link SceneTransition} when changing {@link Scenes} without specifying a {@link SceneTransition}.
     *
     * @see #switchTo(Class)
     */
    Scenes setDefaultTransition(SceneTransition transition);

    /**
     * Adds a {@link Scene}. If the {@link Scene} is already present the {@link Scene} will be replaced.
     */
    Scenes addOrReplace(Scene scene);

    /**
     * Returns {@code true} if the scene of the specified class is present.
     *
     * @param sceneClass class of the {@link Scene}
     * @return true if {@link Scene} is present
     */
    boolean exists(Class<? extends Scene> sceneClass);

    /**
     * Adds one ore multiple {@link Scene scenes}. These {@link Scene scenes} are than available to switch into.
     *
     * @see #switchTo(Class)
     */
    Scenes add(Scene... scenes);

    /**
     * Switches directly to another existing {@link Scene} that has previously been added without using a {@link SceneTransition}.
     *
     * @see #switchTo(Class, SceneTransition)
     * @see #add(Scene...)
     */
    Scenes switchTo(Class<? extends Scene> sceneClass);

    /**
     * Switches to another existing {@link Scene} that has previously been added. Uses the specified {@link SceneTransition}
     * to add some charm.
     *
     * @see #switchTo(Class)
     * @see #switchTo(Class, Supplier)
     * @see #add(Scene...)
     */
    Scenes switchTo(Class<? extends Scene> sceneClass, SceneTransition transition);

    /**
     * Switches to another existing {@link Scene} that has previously been added. Uses the specified {@link SceneTransition}
     * to add some charm.
     *
     * @see #switchTo(Class)
     * @see #switchTo(Class, SceneTransition)
     * @see #add(Scene...)
     */
    default Scenes switchTo(Class<? extends Scene> sceneClass, Supplier<SceneTransition> transition) {
        return switchTo(sceneClass, transition.get());
    }

    /**
     * Returns {@code true} if there is currently a {@link SceneTransition} in progress.
     */
    boolean isTransitioning();

    /**
     * Removes a previously added {@link Scene}. Throws an {@link IllegalArgumentException} if the specified {@link Scene }
     * is not present. Also you cannot remove the currently active {@link Scene}.
     */
    Scenes remove(Class<? extends Scene> sceneClass);

    /**
     * Returns {@code true} if the specified {@link Scene} is currently active.
     */
    boolean isActive(Class<? extends Scene> sceneClass);

    /**
     * Returns the class of the current {@link Scene}.
     */
    Class<? extends Scene> activeScene();

    /**
     * Returns the current scene count.
     */
    int sceneCount();

    /**
     * Returns the {@link Environment} of the specified {@link Scene}. Every {@link Scene} uses it's own {@link Environment}.
     * The {@link Environment} cannot be shared between {@link Scene Scenes}.
     */
    Environment environmentOf(Class<? extends Scene> sceneClass);

    /**
     * Specify the {@link Scene} that is used when loading another {@link Scene} or when while the {@link Engine} is warming up.
     * <p>
     * When not specified {@link DefaultLoadingScene} will be used.
     *
     * @see DefaultLoadingScene
     * @see Engine#isWarmedUp()
     */
    Scenes setLoadingScene(Scene loadingScene);

}

