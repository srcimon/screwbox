package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.smoke.Smoke;

import static dev.screwbox.core.environment.Order.PRESENTATION_SMOKE;

//TODO document test
//TODO add to feature smoke?
//TODO OptimizeLightPerformanceSystem
//TODO Smoke VortexComponent (Rotates smoke)
@ExecutionOrder(PRESENTATION_SMOKE)
public class SmokeRenderSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        final Graphics graphics = engine.graphics();
        final Smoke smoke = graphics.smoke();

        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeObstacleComponent.class))) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), 128)) {
                smoke.addObstacle(entity.bounds());
            }
        }

        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokeEmitterComponent.class))) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), 128)) {
                smoke.emit(entity.position(), entity.get(SmokeEmitterComponent.class).amount * engine.loop().delta(), entity.get(SmokeEmitterComponent.class).color);
            }
        }
//TODO split into SmokeConstantPusherComponent
        for (final var entity : engine.environment().fetchAll(Archetype.ofSpacial(SmokePusherComponent.class))) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), 128)) {
                var affector = entity.get(SmokePusherComponent.class);
                Vector speed = affector.speed == null ? entity.get(PhysicsComponent.class).velocity.multiply(0.1) : affector.speed;
                smoke.push(entity.position(), speed.multiply(engine.loop().delta()));
            }
        }

        final var delta = engine.loop().delta();
        smoke.render(delta);
    }
}
