package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(world.visibleArea()).thenReturn(Bounds.$$(0, 0, 100, 100));

        particles.update();
        particles.spawn(Vector.$(20, 10), new ParticleOptions());

        assertThat(particles.particleCount()).isEqualTo(3);
    }

    @Test
    void setParticleLimit_setsParticleLimit() {
        particles.setParticleLimit(20);

        assertThat(particles.particleLimit()).isEqualTo(20);
    }
}
