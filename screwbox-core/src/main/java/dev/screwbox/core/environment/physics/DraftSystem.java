package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.particles.ParticleComponent;

import java.util.List;

import static java.util.Objects.isNull;

public class DraftSystem implements EntitySystem {

    private static final Archetype EFFECTORS = Archetype.ofSpacial(DraftSourceComponent.class);
    private static final Archetype PHYSICS = Archetype.ofSpacial(ParticleComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var effectors = engine.environment().fetchAll(EFFECTORS);
        if (effectors.isEmpty()) {
            return;
        }
        final var physics = engine.environment().fetchAll(PHYSICS);
        for (final var effector : effectors) {
            applyVelocityOnPhysics(physics, effector, engine.loop().delta());
        }
    }

    private void applyVelocityOnPhysics(final List<Entity> physics, final Entity effector, final double delta) {
        final var effect = effector.get(DraftSourceComponent.class);
        if (isNull(effect.lastPosition)) {
            effect.lastPosition = effector.position();
        }
        final var velocity = effector.position()
                .substract(effect.lastPosition)
                .multiply(1 / delta * effect.modifier.value());

        effect.lastPosition = effector.position();
        if (!velocity.isZero()) {
            final var interactionBounds = effector.bounds().expand(effect.range);
            for (final var particle : physics) {
                if (particle.bounds().intersects(interactionBounds)) {
                    final var physicsComponent = particle.get(PhysicsComponent.class);
                    if (physicsComponent.velocity.length() < velocity.length()) {
                        physicsComponent.velocity = physicsComponent.velocity.add(velocity.multiply(delta));
                    }
                }
            }
        }
    }
}
