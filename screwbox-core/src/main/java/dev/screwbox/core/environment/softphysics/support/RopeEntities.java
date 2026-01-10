package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface RopeEntities extends EntityCollection {

    Entity end();

    Entity center();

    List<Entity> connectors();
}
