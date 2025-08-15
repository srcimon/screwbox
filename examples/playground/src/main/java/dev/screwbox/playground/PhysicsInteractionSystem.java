package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class PhysicsInteractionSystem implements EntitySystem {

    private Entity grabbed;

    @Override
    public void update(Engine engine) {
        if(engine.mouse().isPressedLeft()) {
            engine.physics().searchAtPosition(engine.mouse().position())
                    .selectAll().forEach(e -> grabbed = e);
        }

        if(grabbed != null) {
            grabbed.get(PhysicsComponent.class).momentum = Vector.zero();
            grabbed.moveTo(engine.mouse().position());
        }
        if(!engine.mouse().isDownLeft()) {
            grabbed = null;
        }
    }
}
