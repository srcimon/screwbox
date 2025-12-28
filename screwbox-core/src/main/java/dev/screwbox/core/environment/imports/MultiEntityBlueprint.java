package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface MultiEntityBlueprint<T> {

    List<Entity> create(T source, ImportContext context);
}
