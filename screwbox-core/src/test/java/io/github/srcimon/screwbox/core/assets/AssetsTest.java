package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.ScrewBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AssetsTest {

    @Spy
    Assets assets;

    @Test
    void prepareEngineAssets_callsPrepareClassPackage() {
        assets.prepareEngineAssets();

        verify(assets).prepareClassPackage(ScrewBox.class);
    }

    @Test
    void prepareEngineAssetsAsync_callsPrepareClassPackageAsync() {
        assets.prepareEngineAssetsAsync();

        verify(assets).prepareClassPackageAsync(ScrewBox.class);
    }

    @Test
    void prepareClassPackageAsync_classNotNull_callsPreparePackageAsync() {
        assets.prepareClassPackageAsync(AssetsTest.class);

        verify(assets).preparePackageAsync("io.github.srcimon.screwbox.core.assets");
    }

    @Test
    void prepareClassPackageAsync_classIsNull_throwsException() {
        assertThatThrownBy(() -> assets.prepareClassPackageAsync(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("class must not be null");
    }

    @Test
    void prepareClassPackage_classNotNull_callsPreparePackage() {
        assets.prepareClassPackage(AssetsTest.class);

        verify(assets).preparePackage("io.github.srcimon.screwbox.core.assets");
    }

    @Test
    void listAssetLocationsInClassPackage_classNotNull_callsListAssetLocationsInPackage() {
        assets.listAssetLocationsInClassPackage(AssetsTest.class);

        verify(assets).listAssetLocationsInPackage("io.github.srcimon.screwbox.core.assets");
    }

}
