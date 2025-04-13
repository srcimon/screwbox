package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.util.List;

import static java.util.Objects.isNull;

public class ParticleInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(ParticleInteractionComponent.class);
    private static final Archetype PARTICLES = Archetype.ofSpacial(ParticleComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        if (interactors.isEmpty()) {
            return;
        }
        final var particles = engine.environment().fetchAll(PARTICLES);
        for (final var interactor : interactors) {
            applyMomentumOnParticles(particles, interactor, engine.loop().delta());
        }
    }

    private void applyMomentumOnParticles(final List<Entity> particles, final Entity interactor, final double delta) {
        final var interaction = interactor.get(ParticleInteractionComponent.class);
        if (isNull(interaction.lastPos)) {
            interaction.lastPos = interactor.position();
        }
        final var momentum = interactor.position()
                .substract(interaction.lastPos)
                .multiply(1 / delta * interaction.modifier.value());

        interaction.lastPos = interactor.position();
        if (!momentum.isZero()) {
            final double momentumLength = momentum.length();
            final var interactionBounds = interactor.bounds().expand(interaction.range);

            for (final var particle : particles) {
                if (particle.bounds().intersects(interactionBounds)) {
                    var physics = particle.get(PhysicsComponent.class);
                    if (physics.momentum.length() < momentumLength) {
                        physics.momentum = physics.momentum.add(momentum.multiply(delta));
                    }
                }
            }
        }
    }
}
