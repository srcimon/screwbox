package dev.screwbox.core.particles;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Vector.$;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ParticlesTest {

    @Spy
    Particles particles;

    @Test
    void spawn_supplierForOptionsUsed_callsSpawnWithOptions() {
        var options = ParticleOptions.unknownSource();
        particles.spawn($(20, 10), () -> options);

        verify(particles).spawn($(20, 10), options);
    }

    @Test
    void spawnMultiple_supplierForOptionsUsed_callsSpawnMultipleWithOptions() {
        var options = ParticleOptions.unknownSource();
        particles.spawnMultiple(5, $(20, 10), () -> options);

        verify(particles).spawnMultiple(5, $(20, 10), options);
    }
}
