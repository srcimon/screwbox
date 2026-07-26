package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static dev.screwbox.core.environment.Order.PRESENTATION_SMOKE;

//TODO document test
//TODO add to feature smoke?
@ExecutionOrder(PRESENTATION_SMOKE)
public class SmokeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeObstacleComponent.class))) {
            if (engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 128)) {
                engine.graphics().smoke().addObstacle(entity.bounds());
            }
        }

        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeEmitterComponent.class))) {
            if (engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 128)) {
                engine.graphics().smoke().emit(entity.position(), entity.get(SmokeEmitterComponent.class).amount * engine.loop().delta(), entity.get(SmokeEmitterComponent.class).color);
            }
        }
//TODO split physics and non physics
        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeAffectorComponent.class))) {
            if (engine.graphics().isWithinDistanceToVisibleArea(entity.position(), 128)) {
                var affector = entity.get(SmokeAffectorComponent.class);
                Vector speed = affector.speed == null ? entity.get(PhysicsComponent.class).velocity.multiply(0.1) : affector.speed;
                engine.graphics().smoke().push(entity.position(), speed.multiply(engine.loop().delta()));
            }
        }

        final var delta = engine.loop().delta();
        engine.graphics().smoke().render(delta);
    }
}
