package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

//TODO DOC AND TEST
public class ParticleBurstSystem implements EntitySystem {

    private static final Archetype EMITTERS_WITH_TIMEOUT = Archetype.of(ParticleEmitterComponent.class, ParticleBurstComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(EMITTERS_WITH_TIMEOUT)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var burst = particleEmitter.get(ParticleBurstComponent.class);
            if (burst.lastStateChange.isUnset()) {
                burst.lastStateChange = engine.loop().lastUpdate();
            }
            final boolean isTimeToChangeState = Duration.between(engine.loop().lastUpdate(), burst.lastStateChange)
                    .isAtLeast(emitter.isEnabled ? burst.burstInterval : burst.idleInterval);
            if (isTimeToChangeState) {
                System.out.println("TRIGGER");
                emitter.isEnabled = !emitter.isEnabled;
                burst.lastStateChange = engine.loop().lastUpdate();
            }
        }
    }

}
