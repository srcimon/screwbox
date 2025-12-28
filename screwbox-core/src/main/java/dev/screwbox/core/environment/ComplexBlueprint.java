package dev.screwbox.core.environment;

import java.util.List;

@FunctionalInterface
public interface ComplexBlueprint<T> {

    List<Entity> create(T source, ImportContext context);
}
