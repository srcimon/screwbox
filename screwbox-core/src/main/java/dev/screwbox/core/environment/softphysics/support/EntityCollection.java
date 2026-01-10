package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface EntityCollection {

    Entity root();

    List<Entity> all();
}
