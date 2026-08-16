package dev.screwbox.core.environment.core;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.scenes.Scene;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks an {@link Component} as bakeable. Bakeable static {@link Entity entities} are merged (baked) together before the {@link Scene} starts.
 *
 * @since 3.35.0
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Bakeable {
}
