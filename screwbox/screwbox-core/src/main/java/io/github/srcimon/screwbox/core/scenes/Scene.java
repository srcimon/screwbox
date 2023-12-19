package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;

public interface Scene {

    default void populate(Environment environment) {
    }

    default void onEnter(Engine engine) {
    }

    default void onExit(Engine engine) {
    }

}
