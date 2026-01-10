package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface ClothEntities extends SoftStructureEntities {

    List<Entity> outline();

    List<Entity> outlineTop();

    List<Entity> outlineBottom();
}
