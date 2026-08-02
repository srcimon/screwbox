package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.smoke.Smoke;

import static dev.screwbox.core.environment.Order.PRESENTATION_SMOKE;

//TODO test

/**
 * Keeps entity components in sync with the smoke simulation and renders {@link Smoke}.
 */
@ExecutionOrder(PRESENTATION_SMOKE)
public class SmokeRenderSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(SmokeObstacleComponent.class);
    private static final Archetype EMITTERS = Archetype.ofSpacial(SmokeEmitterComponent.class);
    private static final Archetype INTERACTORS = Archetype.ofSpacial(SmokeInteractionComponent.class, PhysicsComponent.class);
    private static final Archetype WINDS = Archetype.ofSpacial(WindComponent.class);

    @Override
    public void update(Engine engine) {
        final var smoke = engine.graphics().smoke();

        // obstacles
        for (final var entity : engine.environment().fetchAll(OBSTACLES)) {
            smoke.addObstacle(entity.bounds());
        }

        // emitters
        for (final var entity : engine.environment().fetchAll(EMITTERS)) {
            final var emitter = entity.get(SmokeEmitterComponent.class);
            smoke.emit(entity.position(), emitter.velocity.multiply(engine.loop().delta()), emitter.amount * engine.loop().delta(), emitter.color);
        }

        // winds
        for (final var entity : engine.environment().fetchAll(WINDS)) {
            final var wind = entity.get(WindComponent.class);
            smoke.pinVelocity(entity.bounds(), wind.velocity);
        }

        // interactors
        for (final var entity : engine.environment().fetchAll(INTERACTORS)) {
            var interaction = entity.get(SmokeInteractionComponent.class);
            final Vector speed = entity.get(PhysicsComponent.class).velocity.multiply(interaction.modifier.value() * engine.loop().delta());
            smoke.push(entity.position(), speed);
        }

        smoke.render(engine.loop().delta());
    }
}
