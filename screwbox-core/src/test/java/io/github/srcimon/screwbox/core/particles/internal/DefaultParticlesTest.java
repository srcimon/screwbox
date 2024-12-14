package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.graphics.internal.AttentionFocus;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class DefaultParticlesTest {

    @Mock
    DefaultEnvironment environment;

    @Mock
    DefaultScenes scenes;

    @Mock
    AttentionFocus attentionFocus;

    @InjectMocks
    DefaultParticles particles;

    @Test
    void particleCount_twoParticlesFoundAtBeginningAndOneSpawned_returnsThree() {
        when(attentionFocus.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);
        when(scenes.activeEnvironment()).thenReturn(environment);
        when(environment.entityCount(Archetype.of(ParticleComponent.class))).thenReturn(2L);

        particles.update();
        particles.spawn($(20, 10), ParticleOptions.unknownSource());

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
        when(scenes.activeEnvironment()).thenReturn(environment);

        particles.spawn($(20, 20), ParticleOptions.unknownSource());

        verifyNoInteractions(attentionFocus);
    }

    @Test
    void spawn_particleOutOfSpawnDistance_doesntSpawnParticle() {
        when(attentionFocus.isWithinDistanceToVisibleArea($(20000, 20), 1000)).thenReturn(false);
        when(scenes.activeEnvironment()).thenReturn(environment);

        particles.spawn($(20000, 20), ParticleOptions.unknownSource());

        assertThat(particles.particleCount()).isZero();
        assertThat(particles.particlesSpawnCount()).isZero();
    }

    @Test
    void spawn_withSourceEntity_spawnsParticleUsingOptions() {
        when(attentionFocus.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);
        when(scenes.activeEnvironment()).thenReturn(environment);
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
        assertThat(tweenComponent.mode).isEqualTo(Ease.LINEAR_OUT);
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
        when(attentionFocus.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);
        when(scenes.activeEnvironment()).thenReturn(environment);

        particles.spawn($$(80, 100, 100, 100), ParticleOptions.unknownSource().drawOrder(50).startScale(4));

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
    void spawnMultiple_spawnTenAndLimitIsFive_spawnsFive() {
        when(scenes.activeEnvironment()).thenReturn(environment);
        when(attentionFocus.isWithinDistanceToVisibleArea(any(), anyDouble())).thenReturn(true);

        particles.setParticleLimit(5);

        particles.spawnMultiple(10, Vector.zero(), ParticleOptions.unknownSource());

        verify(environment, times(5)).addEntity(any(Entity.class));
        assertThat(particles.particleCount()).isEqualTo(5);
        assertThat(particles.particlesSpawnCount()).isEqualTo(5);
    }

    @Test
    void setSpawnDistance_setsSpawnDistance() {
        particles.setSpawnDistance(40);

        assertThat(particles.spawnDistance()).isEqualTo(40);
    }

    @Test
    void isWithinSpawnArea_distanceTooBig_isFalse() {
        when(attentionFocus.isWithinDistanceToVisibleArea($(100, 1000), 1000)).thenReturn(false);

        assertThat(particles.isWithinSpawnArea($(100, 1000))).isFalse();
    }

    @Test
    void isWithinSpawnArea_distanceWithinRange_isTrue() {
        when(attentionFocus.isWithinDistanceToVisibleArea($(100, 1000), 1000)).thenReturn(true);

        assertThat(particles.isWithinSpawnArea($(100, 1000))).isTrue();
    }
}
