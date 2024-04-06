package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FontsBundleTest {

    @ParameterizedTest
    @EnumSource(FontsBundle.class)
    void testAllAssetsCanBeLoaded(FontsBundle bundle) {
        assertThat(bundle.get()).isNotNull();
    }

    @Test
    void getWhite_returnsWhiteAsset() {
        var sprite = FontsBundle.BOLDZILLA.getWhite().spriteFor('A').singleFrame();
        assertThat(sprite.colorAt(4, 4)).isEqualTo(Color.WHITE);
    }

}
