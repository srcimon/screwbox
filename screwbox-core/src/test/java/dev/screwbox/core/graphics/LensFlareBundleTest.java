package dev.screwbox.core.graphics;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class LensFlareBundleTest {

    @ParameterizedTest
    @EnumSource(LensFlareBundle.class)
    void testAllAssetsCanBeLoaded(LensFlareBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }
}
