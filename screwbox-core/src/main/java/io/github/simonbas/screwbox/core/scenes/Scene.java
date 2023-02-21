package io.github.simonbas.screwbox.core.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;

public interface Scene {

    default void initialize(Entities entities) {
    }

    default void onEnter(Engine engine) {
    }

    default void onExit(Engine engine) {
    }

}
