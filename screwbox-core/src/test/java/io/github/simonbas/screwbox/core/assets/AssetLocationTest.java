package io.github.simonbas.screwbox.core.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class AssetLocationTest {

    private static final Asset<String> ASSET = Asset.asset(() -> "test");

    AssetLocation assetLocation;

    @BeforeEach
    void beforeEach() throws Exception {
        Field assetField = AssetLocationTest.class.getDeclaredField("ASSET");
        assetLocation = AssetLocation.tryToCreateAt(assetField).get();
        ASSET.load();
    }

    @Test
    void toString_returnsAssetLocationId() {
        assertThat(assetLocation)
                .hasToString(
                        "AssetLocation [id=io.github.simonbas.screwbox.core.assets.AssetLocationTest.ASSET, isLoaded=true]");
    }
}
