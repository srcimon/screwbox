package de.suzufa.screwbox.core.assets;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetLocationTest {

    private static final Asset<String> ASSET = Asset.asset(() -> "test");

    AssetLocation assetLocation;

    @BeforeEach
    void beforeEach() throws Exception {
        Field assetField = AssetLocationTest.class.getDeclaredField("ASSET");
        assetLocation = AssetLocation.tryToCreateAt(assetField).get();
    }

    @Test
    void toString_returnsAssetLocationId() {
        assertThat(assetLocation)
                .hasToString(
                        "AssetLocation [id=de.suzufa.screwbox.core.assets.AssetLocationTest.ASSET, isLoaded=false]");
    }
}
