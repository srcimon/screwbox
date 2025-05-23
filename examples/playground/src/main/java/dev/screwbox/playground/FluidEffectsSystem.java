package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;
import java.util.Random;

public class FluidEffectsSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidEffectsComponent.class, FluidComponent.class);
    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(Engine engine) {

        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            final var effects = entity.get(FluidEffectsComponent.class);
            if (effects.scheduler.isTick()) {
                final List<Entity> physics = engine.environment().fetchAll(PHYSICS);
                final var surfaceNodes = entity.get(FluidComponent.class).surface.nodes();
                for (var physicsEntity : physics) {
                    Double y = null;
                    PhysicsComponent physicsComponent = physicsEntity.get(PhysicsComponent.class);
                    if (!physicsComponent.ignoreCollisions && physicsComponent.momentum.length() > effects.speedThreshold) {
                        for (final var node : surfaceNodes) {
                            if (physicsEntity.bounds().contains(node)) {
                                y = node.y();
                            }
                            //TODO optimize performance
//TODO ParticleOptions.relativeOrigin(+1)
                        }
                    }
                    if (y != null) {
                        Bounds bounds = Bounds.atOrigin(physicsEntity.bounds().minX(), y, physicsEntity.bounds().width(), 2);
                        engine.particles().spawn(bounds, SpawnMode.BOTTOM_SIDE, effects.particleOptions.drawOrder(entity.get(RenderComponent.class) == null ? 0 : entity.get(RenderComponent.class).drawOrder + 1));
                        if (effects.soundScheduler.isTick()) {//TODO move sheduler out of other sceduler
                            engine.audio().playSound(ListUtil.randomFrom(effects.sounds), SoundOptions.playOnce()
                                    .speed(RANDOM.nextDouble(0.6, 1.2))//TODO configure ranges in splashcomponent
                                    .position(physicsEntity.position())
                            );
                        }
                    }
                }
            }
        }
    }
}
