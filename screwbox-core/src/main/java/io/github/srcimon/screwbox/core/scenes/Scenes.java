package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.environment.Environment;

public interface Scenes {

    Scenes addOrReplace(Scene scene);

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
