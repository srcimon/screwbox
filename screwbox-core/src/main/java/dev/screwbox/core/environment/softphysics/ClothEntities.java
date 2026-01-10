package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;

import java.util.List;

public interface ClothEntities extends SoftPhysicsEntities {

    List<Entity> outline();

    List<Entity> outlineTop();

    List<Entity> outlineBottom();
}
