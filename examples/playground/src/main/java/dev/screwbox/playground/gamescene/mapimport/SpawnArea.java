package dev.screwbox.playground.gamescene.mapimport;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.playground.gamescene.spawn.SpawnComponent;
import dev.screwbox.tiled.GameObject;

public class SpawnArea implements Blueprint<GameObject> {
    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity()
            .bounds(object.bounds())
            .add(new SpawnComponent(), config -> config.team = object.properties().getColor("team"));
    }
}
