package dev.screwbox.core.environment;

import java.util.List;

@FunctionalInterface
public interface ComplexBlueprint<T> {

    List<Entity> assembleFrom(T source, ImportContext context);
}
