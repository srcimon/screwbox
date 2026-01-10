package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

//TODO use
public interface SoftBodyEntities extends EntityCollection {

    List<Entity> supportOrigins();

    List<Entity> supportTargets();
}
