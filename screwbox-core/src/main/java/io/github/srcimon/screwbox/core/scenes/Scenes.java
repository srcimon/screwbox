package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;

/**
 * Manage different game situations like pause or options in different {@link Scenes}.
 * <p/>
 * The {@link Engine} is always started with a {@link DefaultScene} present.
 */
public interface Scenes {

    //TODO add JavaDoc
    Scenes addOrReplace(Scene scene);

    /**
     * Returns {@code true} if the scene of the specified class is present.
     *
     * @param sceneClass class of the {@link Scene}
     * @return true if {@link Scene} is present
     */
    boolean contains(Class<? extends Scene> sceneClass);

    //TODO add JavaDoc
    Scenes add(Scene... scenes);

    //TODO add JavaDoc
    Scenes switchTo(Class<? extends Scene> sceneClass);

    //TODO: implement (#231)
    //Scenes switchToUsingTransition(Class<? extends Scene> sceneClass, SceneTransition.in(Fading).inDurationMillis(40).out();

    //TODO add JavaDoc
    Scenes remove(Class<? extends Scene> sceneClass);

    //TODO add JavaDoc
    boolean isActive(Class<? extends Scene> sceneClass);

    //TODO add JavaDoc
    Class<? extends Scene> activeScene();

    //TODO add JavaDoc
    int sceneCount();

    //TODO add JavaDoc
    Environment environmentOf(Class<? extends Scene> sceneClass);

    //TODO add JavaDoc

    /**
     * Specify the {@link Scene} that is used when loading another {@link Scene} or when while the {@link Engine} is warming up.
     * <p/>
     * When not specified {@link DefaultLoadingScene} will be used.
     *
     * @see DefaultLoadingScene
     * @see Engine#isWarmedUp()
     */
    Scenes setLoadingScene(Scene loadingScene);
}

