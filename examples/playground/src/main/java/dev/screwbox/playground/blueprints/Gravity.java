package dev.screwbox.playground.blueprints;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.imports.SimpleBlueprint;
import dev.screwbox.core.environment.physics.GravityComponent;

public class Gravity implements SimpleBlueprint {

    @Override
    public Entity create() {
        return new Entity()
                .name("gravity")
                .add(new GravityComponent(Vector.y(400)));
    }
}
