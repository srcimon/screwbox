package dev.screwbox.core.environment.sourceimport;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface ComplexBlueprint<T> {

    List<Entity> create(T source, ImportContext context);
}
