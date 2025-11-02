package dev.screwbox.core.environment;

import dev.screwbox.core.loop.Loop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the order of the execution of an {@link EntitySystem} in the game {@link Loop}.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface HasOrder {//TODO rename to SystemConfig.Order

    Order value();

}
