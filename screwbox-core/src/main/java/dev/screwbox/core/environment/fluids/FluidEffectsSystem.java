package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.particles.SpawnMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

//TODO document
//TODO add to fluid guide
public class FluidEffectsSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidEffectsComponent.class, FluidComponent.class);
    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Random RANDOM = new Random();
    private static final Map<Entity, Playback> playbacks = new HashMap<>();

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            final var config = entity.get(FluidEffectsComponent.class);
            if (config.scheduler.isTick()) {
                final var surfaceNodes = entity.get(FluidComponent.class).surface.nodes();
                applyEffects(config, surfaceNodes, engine);
            }
        }
        if (!playbacks.isEmpty()) {
            playbacks.entrySet().removeIf(playback -> !engine.audio().isPlaybackActive(playback.getValue()));
        }
    }

    //TODO fix one playback per physics entity not one per fluid!
    private void applyEffects(final FluidEffectsComponent effects, final List<Vector> surfaceNodes, final Engine engine) {
        final List<Entity> physics = engine.environment().fetchAll(PHYSICS);
        for (final var physicsEntity : physics) {
            fetchInteractingNode(physicsEntity, effects.speedThreshold, surfaceNodes).ifPresent(node -> {
                // particles
                if (nonNull(effects.particleOptions)) {
                    final Bounds bounds = physicsEntity.bounds();
                    final Particles particles = engine.particles();
                    Bounds effectBounds = Bounds.atOrigin(bounds.minX(), node.y(), bounds.width(), 1);
                    particles.spawn(effectBounds, SpawnMode.BOTTOM_SIDE, effects.particleOptions);
                }

                // Sound
                final var playback = playbacks.get(physicsEntity);
                if (isNull(playback) || !engine.audio().isPlaybackActive(playback)) {
                    //TODO check if no sound is null
                    Sound sound = RANDOM.nextBoolean() ? effects.primarySound : effects.secondarySound;
                    playbacks.put(physicsEntity, engine.audio().playSound(sound, SoundOptions.playOnce()
                            .speed(RANDOM.nextDouble(effects.minAudioSpeed, effects.maxAudioSpeed))
                            .position(physicsEntity.position())));
                }
            });
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
