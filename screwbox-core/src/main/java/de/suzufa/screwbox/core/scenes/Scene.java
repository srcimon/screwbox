package de.suzufa.screwbox.core.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntityEngine;

public interface Scene {

    default void initialize(EntityEngine entityEngine) {
    }

    default void onEnter(Engine engine) {
    }

    default void onExit(Engine engine) {
    }

}
