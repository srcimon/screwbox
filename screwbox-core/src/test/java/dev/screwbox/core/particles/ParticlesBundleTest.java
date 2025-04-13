package dev.screwbox.core.particles;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ParticlesBundleTest {

    @ParameterizedTest
    @EnumSource(ParticlesBundle.class)
    void testAllAssetsCanBeLoaded(ParticlesBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
