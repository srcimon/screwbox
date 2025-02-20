package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

/**
 * Finds particle emitters that have a {@link ParticleBurstComponent} and shut them down if they are active for too long.
 */
@Order(Order.SystemOrder.SIMULATION_EARLY)
public class ParticleBurstSystem implements EntitySystem {

    private static final Archetype EMITTERS_WITH_TIMEOUT = Archetype.of(ParticleEmitterComponent.class, ParticleBurstComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(EMITTERS_WITH_TIMEOUT)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var burst = particleEmitter.get(ParticleBurstComponent.class);
            if (emitter.isEnabled) {
                if (burst.activeSince.isUnset()) {
                    burst.activeSince = engine.loop().time();
                }
                final boolean isTimeToChangeState = Duration.between(engine.loop().time(), burst.activeSince).isAtLeast(burst.timeout);
                if (isTimeToChangeState) {
                    emitter.isEnabled = false;
                    burst.activeSince = Time.unset();
                }
            }
        }
    }

}
