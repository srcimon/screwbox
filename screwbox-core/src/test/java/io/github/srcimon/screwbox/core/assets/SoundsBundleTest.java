package io.github.srcimon.screwbox.core.assets;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class SoundsBundleTest {

    @ParameterizedTest
    @EnumSource(SoundsBundle.class)
    void testAllAssetsCanBeLoaded(SoundsBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
