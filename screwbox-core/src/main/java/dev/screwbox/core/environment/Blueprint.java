package dev.screwbox.core.environment;

@FunctionalInterface
public interface Blueprint<T> {

    Entity assembleFrom(T source);

}
