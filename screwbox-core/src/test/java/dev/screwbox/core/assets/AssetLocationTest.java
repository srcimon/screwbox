package dev.screwbox.core.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssetLocationTest {

    private static final Asset<String> ASSET = Asset.asset(() -> "test");

    AssetLocation assetLocation;

    @BeforeEach
    void beforeEach() throws Exception {
        final var assetField = AssetLocationTest.class.getDeclaredField("ASSET");
        final var optionalAssetLocation = AssetLocation.tryToCreateAt(assetField);
        assertThat(optionalAssetLocation).isPresent();
        assetLocation = optionalAssetLocation.get();
        ASSET.load();
    }

    @Test
    void toString_returnsAssetLocationId() {
        assertThat(assetLocation)
                .hasToString("AssetLocation [id=dev.screwbox.core.assets.AssetLocationTest.ASSET, isLoaded=true]");
    }
}
