package dev.screwbox.core.environment;

@FunctionalInterface
public interface AdvancedBlueprint<T> {

    Entity create(T source, ImportContext context);

}
