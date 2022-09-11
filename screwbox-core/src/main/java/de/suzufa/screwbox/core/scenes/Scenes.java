package de.suzufa.screwbox.core.scenes;

import de.suzufa.screwbox.core.entities.EntityEngine;

public interface Scenes {

    Scenes add(Scene scene);

    Scenes add(Scene... scenes);

    Scenes switchTo(Class<? extends Scene> sceneClass);

    Scenes remove(Class<? extends Scene> sceneClass);

    boolean isActive(Class<? extends Scene> sceneClass);

    Class<? extends Scene> activeScene();

    int sceneCount();

    EntityEngine entityEngineOf(Class<? extends Scene> sceneClass);

}
