package dev.screwbox.core.environment;

@FunctionalInterface
public interface Blueprint<T> {

    Entity create(T source);

}
