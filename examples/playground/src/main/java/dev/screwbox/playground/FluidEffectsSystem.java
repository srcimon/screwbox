package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.particles.SpawnMode;

import java.util.Random;

public class FluidEffectsSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidEffectsComponent.class, FluidComponent.class);
    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            var fluid = entity.get(FluidComponent.class);
            var effects = entity.get(FluidEffectsComponent.class);
            if (effects.scheduler.isTick()) {
                final var physicsEntities = engine.environment().fetchAll(PHYSICS);
                for (var node : fluid.surface.nodes()) {
                    for (var physicsEntity : physicsEntities) {
                        //TODO optimize performance

                        boolean contains = physicsEntity.bounds().contains(node);
                        if (contains && physicsEntity.get(PhysicsComponent.class).momentum.length() > 15) {//TODO configure threshold
                            Bounds bounds = Bounds.atOrigin(physicsEntity.bounds().minX(), node.y(), physicsEntity.bounds().width(), 2);
                            engine.particles().spawn(bounds, SpawnMode.BOTTOM_SIDE, effects.particleOptions.source(entity));
                        }
                    }
                }

            }
        }
    }
}
