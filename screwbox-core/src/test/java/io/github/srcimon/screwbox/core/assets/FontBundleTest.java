package io.github.srcimon.screwbox.core.assets;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class FontBundleTest {

    @ParameterizedTest
    @EnumSource(FontBundle.class)
    void testAllAssetsCanBeLoaded(FontBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
