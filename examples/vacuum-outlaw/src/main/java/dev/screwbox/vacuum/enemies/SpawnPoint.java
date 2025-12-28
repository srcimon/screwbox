package dev.screwbox.vacuum.enemies;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiled.GameObject;

public class SpawnPoint implements Blueprint<GameObject> {

    @Override
    public Entity create(GameObject object) {
        return new Entity(object.id()).name("spawnpoint")
                .add(new SpawnPointComponent(object.layer().order()))
                .add(new TransformComponent(object.position()));
    }
}
