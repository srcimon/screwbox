package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.test.EnvironmentExtension;
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

        when(loop.time()).thenReturn(
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

        when(loop.time()).thenReturn(Time.now());

        environment.update();

        assertThat(emitter.isEnabled).isTrue();
    }
}
