package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.loop.Loop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the order of the execution of an {@link EntitySystem} in the game {@link Loop}.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Order {

    SystemOrder value();

    /**
     * The order of execution of an {@link EntitySystem}.
     */
    enum SystemOrder {
        OPTIMIZATION,
        PREPARATION,
        SIMULATION_BEGIN,
        SIMULATION,
        SIMULATION_LATE,
        PRESENTATION_PREPARE,
        PRESENTATION_BACKGROUND,
        PRESENTATION_WORLD,
        PRESENTATION_EFFECTS,
        PRESENTATION_LIGHT,
        PRESENTATION_ON_TOP_OF_LIGHT,
        PRESENTATION_OVERLAY,
        PRESENTATION_TRANSITIONS,
        PRESENTATION_UI_BACKGROUND,
        PRESENTATION_UI,
        PRESENTATION_UI_FOREGROUND,
        SCENE_TRANSITIONS
    }
}
