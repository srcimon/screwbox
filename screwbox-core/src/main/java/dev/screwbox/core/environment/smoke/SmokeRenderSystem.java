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
//TODO OptimizeLightPerformanceSystem
//TODO Smoke VortexComponent (Rotates smoke)
//TODO Split into rendering and interaction system
//TODO WindCompomnent
@ExecutionOrder(PRESENTATION_SMOKE)
public class SmokeRenderSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(SmokeObstacleComponent.class);
    private static final Archetype EMITTERS = Archetype.ofSpacial(SmokeEmitterComponent.class);
    private static final Archetype PUSHERS = Archetype.ofSpacial(SmokePusherComponent.class);

    @Override
    public void update(Engine engine) {
        final Graphics graphics = engine.graphics();
        final Smoke smoke = graphics.smoke();
        final int renderingDistance = graphics.configuration().smokeCellPadding() * graphics.configuration().smokeCellSize();

        for (final var entity : engine.environment().fetchAll(OBSTACLES)) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), renderingDistance)) {
                smoke.addObstacle(entity.bounds());
            }
        }

        for (final var entity : engine.environment().fetchAll(EMITTERS)) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), renderingDistance)) {
                final var emitter = entity.get(SmokeEmitterComponent.class);
                smoke.emit(entity.position(), emitter.velocity.multiply(engine.loop().delta()), emitter.amount * engine.loop().delta(), emitter.color);
            }
        }
//TODO split into SmokeConstantPusherComponent

        for (final var entity : engine.environment().fetchAll(PUSHERS)) {
            if (graphics.isWithinDistanceToVisibleArea(entity.position(), renderingDistance)) {
                var affector = entity.get(SmokePusherComponent.class);
                Vector speed = affector.speed == null ? entity.get(PhysicsComponent.class).velocity.multiply(0.1) : affector.speed;
                smoke.push(entity.position(), speed.multiply(engine.loop().delta()));
            }
        }

        final var delta = engine.loop().delta();
        smoke.render(delta);
    }
}
