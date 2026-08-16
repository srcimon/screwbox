package dev.screwbox.core.environment.core;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//TODO document
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface CanBeBaked {
}
