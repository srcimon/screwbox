package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class ParticleInteractionSystem implements EntitySystem {

    private final Archetype INTERACTORS = Archetype.of(ParticleInteractionComponent.class, TransformComponent.class, PhysicsComponent.class);
    private final Archetype PARTICLES = Archetype.of(ParticleComponent.class, TransformComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var interactor : engine.environment().fetchAll(INTERACTORS)) {
            var momentum = interactor.get(PhysicsComponent.class).momentum;
            if(!momentum.isZero()) {
                final var particlesInRange = engine.physics()
                        .searchInRange(interactor.bounds().expand(interactor.get(ParticleInteractionComponent.class).range))
                        .checkingFor(PARTICLES)
                        .selectAll();

                for(final var particle : particlesInRange) {
                    var physics = particle.get(PhysicsComponent.class);
                    if (physics.momentum.length() < momentum.multiply(0.5).length()) {
                        physics.momentum = physics.momentum.add(momentum.multiply(engine.loop().delta()));
                    }
                    var chaotic = particle.get(ChaoticMovementComponent.class);
                    if (chaotic != null && chaotic.baseSpeed.multiply(0.5).length() < momentum.length()) {
                        chaotic.baseSpeed = chaotic.baseSpeed.add(momentum.multiply(engine.loop().delta()));
                    }
                }
            }
        }
    }
}
