package dev.screwbox.vacuum.enemies;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.tiles.GameObject;

public class SpawnPoint implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("spawnpoint")
                .add(new SpawnPointComponent(object.layer().order()))
                .add(new TransformComponent(object.position()));
    }
}
