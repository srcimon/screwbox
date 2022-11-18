package de.suzufa.screwbox.core.assets.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.async.Async;
import de.suzufa.screwbox.core.log.Log;

@ExtendWith(MockitoExtension.class)
class DefaultAssetsTest {

    @Mock
    Async async;

    @Mock
    Log log;

    @InjectMocks
    DefaultAssets assets;

    private static final Asset<String> ASSET_A = Asset.asset(() -> "loaded");
    private static final Asset<String> ASSET_B = Asset.asset(() -> "loaded");

    @Test
    void listAssetLocationsInPackage_packageDoesntExist_emptyList() {
        var locations = assets.listAssetLocationsInPackage("de.suzufa.unknown");

        assertThat(locations).isEmpty();
    }

    @Test
    void listAssetLocationsInPackage_noAssetsInPackage_emptyList() {
        var locations = assets.listAssetLocationsInPackage("de.suzufa.core.audio");

        assertThat(locations).isEmpty();
    }

    @Test
    void listAssetLocationsInPackage_packageExists_listsLocations() {
        var locations = assets.listAssetLocationsInPackage("de.suzufa.screwbox.core.assets.internal");

        assertThat(locations).hasSize(2)
                .allMatch(asset -> asset.sourceClass().equals(DefaultAssetsTest.class))
                .anyMatch(asset -> asset.asset().equals(ASSET_A))
                .anyMatch(asset -> asset.sourceField().getName().equals("ASSET_A"))
                .anyMatch(asset -> asset.asset().equals(ASSET_B))
                .anyMatch(asset -> asset.sourceField().getName().equals("ASSET_B"));
    }

    @Test
    void preparePackage_assetsNotLoaded_assetsLoadedAfterwards() {
        assertThat(ASSET_A.isLoaded()).isFalse();
        assertThat(ASSET_B.isLoaded()).isFalse();

        assets.preparePackage("de.suzufa.screwbox.core.assets.internal");

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();
    }
}
