package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class ParticleInteractionSystem implements EntitySystem {

    private final Archetype INTERACTORS = Archetype.of(ParticleInteractionComponent.class, TransformComponent.class, PhysicsComponent.class);
    private final Archetype PARTICLES = Archetype.of(ParticleComponent.class, TransformComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var interactor : engine.environment().fetchAll(INTERACTORS)) {
            final var interaction = interactor.get(ParticleInteractionComponent.class);
            if(interaction.lastPos == null) {
                interaction.lastPos = interactor.position();
            }
            var momentum = interactor.position().substract(interaction.lastPos).multiply(1 / engine.loop().delta())
                    .multiply(interaction.modifier.value());
            interaction.lastPos = interactor.position();
            if(!momentum.isZero()) {
                final var particlesInRange = engine.physics()
                        .searchInRange(interactor.bounds().expand(interaction.range))
                        .checkingFor(PARTICLES)
                        .selectAll();

                for(final var particle : particlesInRange) {
                    var physics = particle.get(PhysicsComponent.class);
                    if (physics.momentum.length() < momentum.length()) {
                        physics.momentum = physics.momentum.add(momentum.multiply(engine.loop().delta()));
                    }
                }
            }
        }
    }
}
