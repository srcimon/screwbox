package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * Spawns particles and plays {@link dev.screwbox.core.audio.Sound sounds} for physics entities interacting with fluids.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 3.3.0
 */
@ExecutionOrder(Order.SIMULATION_LATE)
public class FluidEffectsSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidEffectsComponent.class, FluidComponent.class);
    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(FLUIDS)) {
            final var config = entity.get(FluidEffectsComponent.class);
            if (config.scheduler.isTick(engine.loop().time())) {
                final var surfaceNodes = entity.get(FluidComponent.class).surface.definitionNotes();
                applyEffects(config, surfaceNodes, engine);
            }
        }
    }

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
                if (!effects.sounds.isEmpty() && !engine.audio().hasActivePlaybacksMatching(alreadyPlayingWaterSoundsNear(effects, physicsEntity.position()))) {
                    engine.audio().playSound(ListUtil.randomFrom(effects.sounds), SoundOptions.playOnce()
                            .randomness(effects.randomness)
                            .position(physicsEntity.position()));
                }
            });
        }
    }

    private static Predicate<Playback> alreadyPlayingWaterSoundsNear(final FluidEffectsComponent effects, final Vector position) {
        return playback -> nonNull(playback.options().position())
                           && effects.sounds.contains(playback.sound())
                           && position.distanceTo(playback.options().position()) < effects.soundSuppressionRange;
    }


    private Optional<Vector> fetchInteractingNode(final Entity physicsEntity, final double speedThreshold, final List<Vector> surfaceNodes) {
        PhysicsComponent physicsComponent = physicsEntity.get(PhysicsComponent.class);
        if (!physicsComponent.ignoreCollisions && physicsComponent.velocity.length() > speedThreshold) {
            for (final var node : surfaceNodes) {
                if (physicsEntity.bounds().contains(node)) {
                    return Optional.of(node);
                }
            }
        }
        return Optional.empty();
    }
}
