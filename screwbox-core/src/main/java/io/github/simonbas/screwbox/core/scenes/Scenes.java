package io.github.simonbas.screwbox.core.scenes;

import io.github.simonbas.screwbox.core.entities.Entities;

public interface Scenes {

    Scenes add(Scene... scenes);

    Scenes switchTo(Class<? extends Scene> sceneClass);

    Scenes remove(Class<? extends Scene> sceneClass);

    boolean isActive(Class<? extends Scene> sceneClass);

    Class<? extends Scene> activeScene();

    int sceneCount();

    Entities entitiesOf(Class<? extends Scene> sceneClass);

}
