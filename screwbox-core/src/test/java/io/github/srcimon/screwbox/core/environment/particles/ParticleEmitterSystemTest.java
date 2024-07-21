package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.particles.Particles;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class ParticleEmitterSystemTest {

    @Test
    void update_emitterSpawningAtPositionPresent_spawnsParticle(DefaultEnvironment environment, Loop loop, Particles particles) {
        when(loop.lastUpdate()).thenReturn(Time.now());

        environment.addSystem(new ParticleEmitterSystem())
                .addEntity(1249,
                        new ParticleEmitterComponent(
                                Duration.ofSeconds(1),
                                ParticleEmitterComponent.SpawnMode.POSITION,
                                ParticleOptions.unknownSource().sprite(Sprite.invisible())),
                        new TransformComponent($(120, 10), 20, 20));

        environment.update();

        var position = ArgumentCaptor.forClass(Bounds.class);
        var options = ArgumentCaptor.forClass(ParticleOptions.class);
        verify(particles).spawn(position.capture(), options.capture());
        assertThat(position.getValue()).isEqualTo($$(120, 10,0,0));
        assertThat(options.getValue().modifierIds()).containsExactly("default-render-sprite");
        assertThat(options.getValue().source().id()).contains(1249);
    }

    @Test
    void update_emitterSpawningInAreaPresent_spawnsParticle(DefaultEnvironment environment, Loop loop, Particles particles) {
        when(loop.lastUpdate()).thenReturn(Time.now());

        environment.addSystem(new ParticleEmitterSystem())
                .addEntity(1249,
                        new ParticleEmitterComponent(
                                Duration.ofSeconds(1),
                                ParticleEmitterComponent.SpawnMode.AREA,
                                ParticleOptions.unknownSource()),
                        new TransformComponent($(120, 10), 20, 20));

        environment.update();

        var bounds = ArgumentCaptor.forClass(Bounds.class);
        var options = ArgumentCaptor.forClass(ParticleOptions.class);
        verify(particles).spawn(bounds.capture(), options.capture());
        assertThat(bounds.getValue()).isEqualTo($$(110, 0, 20, 20));
        assertThat(options.getValue().modifierIds()).isEmpty();
        assertThat(options.getValue().source().id()).contains(1249);
    }

    @Test
    void update_disabledEmitterPresent_doenstSpawnParticle(DefaultEnvironment environment, Loop loop, Particles particles) {
        when(loop.lastUpdate()).thenReturn(Time.now());

        ParticleEmitterComponent particleEmitterComponent = new ParticleEmitterComponent(
                Duration.ofSeconds(1),
                ParticleEmitterComponent.SpawnMode.AREA,
                ParticleOptions.unknownSource());
        particleEmitterComponent.isEnabled = false;

        environment.addSystem(new ParticleEmitterSystem())
                .addEntity(1249,
                        particleEmitterComponent,
                        new TransformComponent($(120, 10), 20, 20));

        environment.update();
        verify(particles, never()).spawn(any(Bounds.class), any(ParticleOptions.class));
        verify(particles, never()).spawn(any(Vector.class), any(ParticleOptions.class));
    }
}
