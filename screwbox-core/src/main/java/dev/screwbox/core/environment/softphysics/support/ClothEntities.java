package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface ClothEntities extends EntityCollection {

    List<Entity> outline();

    List<Entity> outlineTop();

    List<Entity> outlineBottom();
}
