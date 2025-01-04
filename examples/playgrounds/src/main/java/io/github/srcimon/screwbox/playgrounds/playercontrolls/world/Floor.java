package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;

import java.util.function.Supplier;

public class Floor implements Supplier<Entity> {

    @Override
    public Entity get() {
        return new Entity("floor")
                .add(new TransformComponent(-150, 24, 600, 8))
                .add(new ColliderComponent());
    }
}
