package io.github.simonbas.screwbox.core.assets;

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
    void prepareClassPackageAsync_classNotNull_callsPreparePackageAsync() {
        assets.prepareClassPackageAsync(AssetsTest.class);

        verify(assets).preparePackageAsync("io.github.simonbas.screwbox.core.assets");
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

        verify(assets).preparePackage("io.github.simonbas.screwbox.core.assets");
    }

    @Test
    void listAssetLocationsInClassPackage_classNotNull_callsListAssetLocationsInPackage() {
        assets.listAssetLocationsInClassPackage(AssetsTest.class);

        verify(assets).listAssetLocationsInPackage("io.github.simonbas.screwbox.core.assets");
    }

}
