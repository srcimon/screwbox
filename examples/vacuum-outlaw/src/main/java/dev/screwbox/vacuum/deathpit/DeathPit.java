package dev.screwbox.vacuum.deathpit;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.tiled.GameObject;

public class DeathPit implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id()).name("deathpit")
                .bounds(object.bounds())
                .add(new ObstacleComponent())
                .add(new DeathpitComponent());
    }
}
