package io.github.srcimon.screwbox.core.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;

/**
 * Structures game sitations in {@link Scene Scenes}. Every {@link Scene} has its own {@link Environment}.
 *
 * @see Scenes#add(Scene...)
 */
public interface Scene {

    /**
     * Populates the {@link Environment} linked to the {@link Scene}. This method is called by the {@link Engine}
     * when the {@link Scene} is added to the {@link Engine#scenes()}.
     */
    void populate(Environment environment);

    /**
     * Overwriting this method allows specific actions when entering a {@link Scene}.
     */
    default void onEnter(final Engine engine) {
    }

    /**
     * Overwriting this method allows specific actions before leaving a {@link Scene}.
     */
    default void onExit(final Engine engine) {
    }

}
