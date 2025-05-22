package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.particles.SpawnMode;

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
                for (var physicsEntity : physicsEntities) {
                    boolean isInside = false;
                    double y=0;
                    if( physicsEntity.get(PhysicsComponent.class).momentum.length() > 15) {
                        for (var node : fluid.surface.nodes()) {
                            if (physicsEntity.bounds().contains(node)) {
                                isInside = true;
                                y = node.y();
                            }
                            //TODO optimize performance

                        }
                    }
                    if (isInside) {//TODO configure threshold
                        Bounds bounds = Bounds.atOrigin(physicsEntity.bounds().minX(), y, physicsEntity.bounds().width(), 2);
                        engine.particles().spawn(bounds, SpawnMode.BOTTOM_SIDE, effects.particleOptions.source(entity));
                    }
                }
            }
        }
    }
}
