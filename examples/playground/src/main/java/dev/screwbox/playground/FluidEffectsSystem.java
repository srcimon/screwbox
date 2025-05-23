package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Audio;
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
import java.util.Optional;
import java.util.Random;

import static java.util.Objects.isNull;

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
                for (final var physicsEntity : physics) {
                    fetchInteractingNode(physicsEntity, effects.speedThreshold, surfaceNodes).ifPresent(node -> {
                        Bounds bounds = Bounds.atOrigin(physicsEntity.bounds().minX(), node.y(), physicsEntity.bounds().width(), 2);
                        engine.particles().spawn(bounds, SpawnMode.BOTTOM_SIDE, effects.particleOptions
                                .drawOrder(entity.get(RenderComponent.class) == null ? 0 : entity.get(RenderComponent.class).drawOrder + 1));    //TODO ParticleOptions.relativeOrigin(+1)
                        Audio audio = engine.audio();
                        applySoundWhenSilent(physicsEntity, effects, audio);
                    });
                }
            }
        }
    }

    private void applySoundWhenSilent(final Entity physicsEntity, final FluidEffectsComponent effects, final Audio audio) {
        if (isNull(effects.playback) || !audio.isPlaybackActive(effects.playback)) {
            effects.playback = audio.playSound(ListUtil.randomFrom(effects.sounds), SoundOptions.playOnce()
                    .speed(RANDOM.nextDouble(effects.minAudioSpeed, effects.maxAudioSpeed))
                    .position(physicsEntity.position()));
        }
    }


    private Optional<Vector> fetchInteractingNode(final Entity physicsEntity, final double speedThreshold, final List<Vector> surfaceNodes) {
        PhysicsComponent physicsComponent = physicsEntity.get(PhysicsComponent.class);
        if (!physicsComponent.ignoreCollisions && physicsComponent.momentum.length() > speedThreshold) {
            for (final var node : surfaceNodes) {
                if (physicsEntity.bounds().contains(node)) {
                    return Optional.of(node);
                }
            }
        }
        return Optional.empty();
    }
}
