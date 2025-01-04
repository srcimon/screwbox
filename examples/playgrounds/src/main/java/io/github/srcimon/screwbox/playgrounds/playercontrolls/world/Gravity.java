package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;

import java.util.function.Supplier;

public class Gravity implements Supplier<Entity> {

    @Override
    public Entity get() {
        return new Entity("gravity")
                .add(new GravityComponent(Vector.y(700)));
    }
}
