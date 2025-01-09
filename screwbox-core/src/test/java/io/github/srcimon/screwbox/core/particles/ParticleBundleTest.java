package io.github.srcimon.screwbox.core.particles;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ParticleBundleTest {

    @ParameterizedTest
    @EnumSource(ParticleBundle.class)
    void testAllAssetsCanBeLoaded(ParticleBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
