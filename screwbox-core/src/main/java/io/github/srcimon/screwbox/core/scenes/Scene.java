package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Entities;

public interface Scene {

    default void initialize(Entities entities) {
    }

    default void onEnter(Engine engine) {
    }

    default void onExit(Engine engine) {
    }

}
