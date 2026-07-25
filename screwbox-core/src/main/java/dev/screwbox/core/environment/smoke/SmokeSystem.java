package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class SmokeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for(final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeEmitterComponent.class))) {
            if(engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 128)) {
                engine.graphics().smoke().emit(entity.position(), entity.get(SmokeEmitterComponent.class).amount , entity.get(SmokeEmitterComponent.class).color);
            }
        }

        for(final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeAffectorComponent.class))) {
            if(engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 128)) {
                var affector = entity.get(SmokeAffectorComponent.class);
                Vector speed = affector.speed == null ? entity.get(PhysicsComponent.class).velocity.multiply(0.1) : affector.speed;
                engine.graphics().smoke().affect(entity.position(), speed);
            }
        }
    }
}
