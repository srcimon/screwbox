package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class SmokeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for(final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeEmitterComponent.class))) {
            engine.graphics().smoke().emit(entity.position(), entity.get(SmokeEmitterComponent.class).amount * engine.loop().delta());
        }

        for(final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeAffectorComponent.class, PhysicsComponent.class))) {
            engine.graphics().smoke().affect(entity.position(), entity.get(PhysicsComponent.class).velocity.multiply(engine.loop().delta()*0.5));
        }
    }
}
