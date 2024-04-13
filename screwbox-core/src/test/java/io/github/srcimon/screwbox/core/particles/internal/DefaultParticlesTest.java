package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultParticlesTest {

    @Mock
    Engine engine;

    @Mock
    Environment environment;

    @Mock
    World world;

    @InjectMocks
    DefaultParticles particles;

    @Test
    void particleCount_twoParticlesFoundAtBeginningAndOneSpawned_returnsThree() {
        when(engine.environment()).thenReturn(environment);
        when(environment.fetchAll(Archetype.of(ParticleComponent.class))).thenReturn(List.of(new Entity(), new Entity()));
        when(world.visibleArea()).thenReturn($$(0, 0, 100, 100));

        particles.update();
        particles.spawn($(20, 10), new ParticleOptions());

        assertThat(particles.particleCount()).isEqualTo(3);
    }

    @Test
    void setParticleLimit_positiveLimit_setsParticleLimit() {
        particles.setParticleLimit(20);

        assertThat(particles.particleLimit()).isEqualTo(20);
    }

    @Test
    void setParticleLimit_negativeLimit_throwsException() {
        assertThatThrownBy(() -> particles.setParticleLimit(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("particle limit must be positive");
    }

    @Test
    void spawn_particleLimitReached_doesntSpawnParticle() {
        particles.setParticleLimit(0);

        particles.spawn($(20, 20), new ParticleOptions());

        verifyNoInteractions(world);
        verifyNoInteractions(environment);
    }

    @Test
    void spawn_particleOutOfSpawnDistance_doesntSpawnParticle() {
        when(world.visibleArea()).thenReturn($$(20, 10, 99, 99));

        particles.spawn($(20000, 20), new ParticleOptions());

        verifyNoInteractions(environment);
        assertThat(particles.particleCount()).isZero();
        assertThat(particles.particlesSpawnCount()).isZero();
    }

    @Test
    void spawn_withSourceEntity_spawnsParticleUsingOptions() {
        when(engine.environment()).thenReturn(environment);
        when(world.visibleArea()).thenReturn($$(20, 10, 99, 99));

        Entity source = new Entity().add(new RenderComponent(20));

        particles.spawn($(80, 100), ParticleOptions.particleSource(source));

        var particleCaptor = ArgumentCaptor.forClass(Entity.class);
        verify(environment).addEntity(particleCaptor.capture());

        assertThat(particles.particleCount()).isOne();
        assertThat(particles.particlesSpawnCount()).isOne();

        Entity particle = particleCaptor.getValue();
        assertThat(particle.name()).contains("particle-1");
        assertThat(particle.hasComponent(TweenDestroyComponent.class)).isTrue();
        assertThat(particle.hasComponent(ParticleComponent.class)).isTrue();

        TweenComponent tweenComponent = particle.get(TweenComponent.class);
        assertThat(tweenComponent.mode).isEqualTo(TweenMode.LINEAR_OUT);
        assertThat(tweenComponent.duration).isEqualTo(Duration.ofSeconds(1));

        PhysicsComponent physicsComponent = particle.get(PhysicsComponent.class);
        assertThat(physicsComponent.ignoreCollisions).isTrue();
        assertThat(physicsComponent.gravityModifier).isZero();
        assertThat(physicsComponent.magnetModifier).isZero();

        RenderComponent renderComponent = particle.get(RenderComponent.class);
        assertThat(renderComponent.sprite).isNotNull();
        assertThat(renderComponent.drawOrder).isEqualTo(20);

        assertThat(particle.bounds()).isEqualTo($$(72, 92, 16, 16));
    }

    @Test
    void spawn_noSourceEntity_spawnsParticleUsingOptions() {
        when(engine.environment()).thenReturn(environment);
        when(world.visibleArea()).thenReturn($$(20, 10, 99, 99));

        particles.spawn($$(80, 100, 100, 100), new ParticleOptions().drawOrder(50).startScale(4));

        var particleCaptor = ArgumentCaptor.forClass(Entity.class);
        verify(environment).addEntity(particleCaptor.capture());

        assertThat(particles.particleCount()).isOne();
        assertThat(particles.particlesSpawnCount()).isOne();

        Entity particle = particleCaptor.getValue();

        RenderComponent renderComponent = particle.get(RenderComponent.class);
        assertThat(renderComponent.drawOrder).isEqualTo(50);
        assertThat(renderComponent.options.scale()).isEqualTo(4);

        assertThat($$(80, 100, 100, 100).contains(particle.position())).isTrue();
    }

    @Test
    void spawnArea_returnsCurrentSpawnArea() {
        particles.setSpawnDistance(30);
        when(world.visibleArea()).thenReturn($$(20, 10, 99, 99));

        assertThat(particles.spawnArea()).isEqualTo($$(5, -5, 129, 129));
    }

    @Test
    void spawnMultiple_spawnTenAndLimitIsFive_spawnsFive() {
        when(engine.environment()).thenReturn(environment);
        when(world.visibleArea()).thenReturn($$(20, 10, 99, 99));

        particles.setParticleLimit(5);

        particles.spawnMultiple(10, Vector.zero(), new ParticleOptions());

        verify(environment, times(5)).addEntity(Mockito.any(Entity.class));
        assertThat(particles.particleCount()).isEqualTo(5);
        assertThat(particles.particlesSpawnCount()).isEqualTo(5);
    }
}
