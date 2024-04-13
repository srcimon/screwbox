package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;

//TODO DOC AND TEST
@Order(SystemOrder.SIMULATION_BEGIN)
public class ParticleBurstSystem implements EntitySystem {

    private static final Archetype EMITTERS_WITH_TIMEOUT = Archetype.of(ParticleEmitterComponent.class, ParticleBurstComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(EMITTERS_WITH_TIMEOUT)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var burst = particleEmitter.get(ParticleBurstComponent.class);
            if(emitter.isEnabled ) {
                if (burst.activeSince.isUnset()) {
                    burst.activeSince = engine.loop().lastUpdate();
                }
                final boolean isTimeToChangeState = Duration.between(engine.loop().lastUpdate(), burst.activeSince).isAtLeast(burst.burstInterval);
                if(isTimeToChangeState) {
                    emitter.isEnabled = false;
                    burst.activeSince = Time.unset();
                }
            }
        }
    }

}
