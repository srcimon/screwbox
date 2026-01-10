package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface RopeEntities extends SoftPhysicsSupport.SoftPhysicsEntities {

    Entity end();

    List<Entity> connectors();
}
