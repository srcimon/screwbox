package dev.screwbox.playground.customizing;

import dev.screwbox.core.environment.Entity;

import java.util.List;

public interface ClothEntities {

    List<Entity> entities();

    Entity root();

    List<Entity> outlineTop();

    List<Entity> outlineBottom();
}
