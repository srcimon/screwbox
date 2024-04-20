package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.environment.Environment;

/**
 * Manage different game situations like pause or options in different {@link Scenes}.
 */
public interface Scenes {

    Scenes addOrReplace(Scene scene);

    /**
     * Returns {@code true} if the scene of the specified class is present.
     *
     * @param sceneClass class of the {@link Scene}
     * @return true if {@link Scene} is present
     */
    boolean contains(Class<? extends Scene> sceneClass);

    Scenes add(Scene... scenes);

    Scenes switchTo(Class<? extends Scene> sceneClass);

    Scenes remove(Class<? extends Scene> sceneClass);

    boolean isActive(Class<? extends Scene> sceneClass);

    Class<? extends Scene> activeScene();

    int sceneCount();

    Environment environmentOf(Class<? extends Scene> sceneClass);

    Scenes setLoadingScene(Scene loadingScene);
}
