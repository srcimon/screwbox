package de.suzufa.screwbox.core.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;

public interface Scene {

    default void initialize(Entities entityEngine) {
    }

    default void onEnter(Engine engine) {
    }

    default void onExit(Engine engine) {
    }

}
