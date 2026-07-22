package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.Audio;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.internal.DefaultEnvironment;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static dev.screwbox.core.Bounds.$$;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class FluidEffectsSystemTest {


    @BeforeEach
    void setUp(DefaultEnvironment environment) {
        environment
                .addSystem(new FluidEffectsSystem())
                .addSystem(new FluidSystem());
    }

    @Test
    void update_noSoundsAndParticlesSpecified_noEffects(DefaultEnvironment environment, Loop loop, Particles particles, Audio audio) {
        when(loop.time()).thenReturn(Time.now().addSeconds(10));

        environment.addEntity(new Entity().name("water")
                        .bounds($$(10, 10, 100, 100))
                        .add(new FluidComponent(10))
                        .add(new FluidEffectsComponent(Collections.emptyList()), config -> config.particleOptions = null))
                .addEntity(new Entity().name("boat")
                        .add(new PhysicsComponent(Vector.of(100, 0)))
                        .bounds($$(0, 0, 20, 20)));

        environment.update();

        verifyNoInteractions(particles);
        verifyNoInteractions(audio);
    }

    @Test
    void update_noInteraction_noEffects(DefaultEnvironment environment, Loop loop, Particles particles, Audio audio) {
        when(loop.time()).thenReturn(Time.now().addSeconds(10));

        environment.addEntity(new Entity().name("water")
                .bounds($$(10, 10, 100, 100))
                .add(new FluidComponent(10))
                .add(new FluidEffectsComponent()));

        environment.update();

        verifyNoInteractions(particles);
        verifyNoInteractions(audio);
    }
}
