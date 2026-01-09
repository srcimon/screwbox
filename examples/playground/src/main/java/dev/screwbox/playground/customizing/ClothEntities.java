package dev.screwbox.playground.customizing;

import dev.screwbox.core.environment.Entity;

import java.util.List;
import java.util.function.Consumer;

public interface ClothEntities {

    List<Entity> entities();

    Entity root();

    ClothEntities root(Consumer<Entity> customizer);

    ClothEntities entities(Consumer<Entity> customizer);
    ClothEntities outlineTop(Consumer<Entity> customizer);
}
