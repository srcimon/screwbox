package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class SpawnPoint implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("spawnpoint")
                .add(new SpawnPointComponent(object.layer().order()))
                .add(new TransformComponent(object.position()));
    }
}
