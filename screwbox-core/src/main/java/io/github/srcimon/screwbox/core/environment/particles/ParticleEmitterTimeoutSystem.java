package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import java.util.Objects;
import java.util.Random;

import static java.util.Objects.isNull;

//TODO DOC AND TEST
public class ParticleEmitterTimeoutSystem implements EntitySystem {

    private static final Archetype EMITTERS_WITH_TIMEOUT = Archetype.of(ParticleEmitterComponent.class, ParticleEmitterTimeoutComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(EMITTERS_WITH_TIMEOUT)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if(emitter.isEnabled) {
                final var timeoutComponent = particleEmitter.get(ParticleEmitterTimeoutComponent.class);
                if(timeoutComponent.lastActive.isUnset()) {
                    timeoutComponent.lastActive = engine.loop().lastUpdate();
                }

                if(Duration.between(engine.loop().lastUpdate(), timeoutComponent.lastActive).isAtLeast(timeoutComponent.timeout)) {
                    emitter.isEnabled = false;
                    timeoutComponent.lastActive = Time.unset();
                }
            }
        }
    }

}
