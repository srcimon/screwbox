package dev.screwbox.core.environment.blueprints;

import dev.screwbox.core.environment.Entity;

public interface SimpleBlueprint {

    Entity create(ImportContext context);
}
