package io.github.srcimon.screwbox.vacuum.deathpit;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Deathpit implements SourceImport.Converter<GameObject> {
    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("deathpit")
                .bounds(object.bounds())
                .add(new PhysicsGridObstacleComponent())
                .add(new DeathpitComponent());
    }
}
