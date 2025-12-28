package dev.screwbox.core.environment;

@FunctionalInterface
public interface AdvancedBlueprint<T> {

    Entity assembleFrom(T source, ImportContext context);

}
