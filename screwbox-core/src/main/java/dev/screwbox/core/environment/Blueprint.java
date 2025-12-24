package dev.screwbox.core.environment;

public interface Blueprint<T> {

    Entity create(T source);
}
