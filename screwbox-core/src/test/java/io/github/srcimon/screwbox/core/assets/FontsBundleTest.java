package io.github.srcimon.screwbox.core.assets;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class FontsBundleTest {

    @ParameterizedTest
    @EnumSource(FontsBundle.class)
    void testAllAssetsCanBeLoaded(FontsBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

}
