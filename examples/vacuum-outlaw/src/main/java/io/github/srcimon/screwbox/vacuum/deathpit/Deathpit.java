package io.github.srcimon.screwbox.vacuum.deathpit;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import dev.screwbox.tiles.GameObject;

public class Deathpit implements SourceImport.Converter<GameObject> {
    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("deathpit")
                .bounds(object.bounds())
                .add(new PhysicsGridObstacleComponent())
                .add(new DeathpitComponent());
    }
}
