package io.github.srcimon.screwbox.core.audio;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class SoundBundleTest {

    @ParameterizedTest
    @EnumSource(SoundBundle.class)
    void testAllAssetsCanBeLoaded(SoundBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
