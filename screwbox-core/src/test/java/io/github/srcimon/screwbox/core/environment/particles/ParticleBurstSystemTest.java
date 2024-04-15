package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ParticleBurstSystemTest {

    @Test
    void update_emitterIsActiveForTooLong_disablesEmitter(DefaultEnvironment environment, Loop loop) {
        var emitter = new ParticleEmitterComponent(Duration.ofMillis(50), ParticleOptions.unknownSource());

        environment.addSystem(new ParticleBurstSystem())
                .addEntity(emitter, new ParticleBurstComponent(Duration.ofMillis(100)));

        when(loop.lastUpdate()).thenReturn(
                Time.now().addSeconds(-20),
                Time.now());

        environment.update();

        assertThat(emitter.isEnabled).isFalse();
    }

    @Test
    void update_emitterIsActiveForShortTime_doesntDisableEmitter(DefaultEnvironment environment, Loop loop) {
        var emitter = new ParticleEmitterComponent(Duration.ofMillis(50), ParticleOptions.unknownSource());

        environment.addSystem(new ParticleBurstSystem())
                .addEntity(emitter, new ParticleBurstComponent(Duration.ofMillis(100)));

        when(loop.lastUpdate()).thenReturn(Time.now());

        environment.update();

        assertThat(emitter.isEnabled).isTrue();
    }
}
