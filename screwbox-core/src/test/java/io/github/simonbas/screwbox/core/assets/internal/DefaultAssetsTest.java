package io.github.simonbas.screwbox.core.assets.internal;

import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.async.Async;
import io.github.simonbas.screwbox.core.log.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    private static final Asset<String> ASSET_C = Asset.asset(() -> "loaded");

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
        var locations = assets.listAssetLocationsInPackage("io.github.simonbas.screwbox.core.assets.internal");

        assertThat(locations).hasSize(3)
                .allMatch(a -> a.loadingDuration().isEmpty())
                .anyMatch(a -> "io.github.simonbas.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "io.github.simonbas.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()))
                .anyMatch(a -> "io.github.simonbas.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_C".equals(a.id()));
    }

    @Test
    void preparePackage_assetsUnloadedEnabledLog_loadsAssetsAndLogs() {
        ASSET_C.load();

        assertThat(ASSET_A.isLoaded()).isFalse();
        assertThat(ASSET_B.isLoaded()).isFalse();

        assets.enableLogging();
        var loadedLocations = assets.preparePackage("io.github.simonbas.screwbox.core.assets.internal");

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();

        var logMessage = ArgumentCaptor.forClass(String.class);
        verify(log).debug(logMessage.capture());
        assertThat(logMessage.getValue()).startsWith("loaded 2 assets in ").endsWith(" ms");

        assertThat(loadedLocations).hasSize(2)
                .allMatch(a -> a.loadingDuration().isPresent())
                .anyMatch(a -> "io.github.simonbas.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "io.github.simonbas.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()));
    }

    @Test
    void preparePackage_loggingDisabled_doesntLog() {
        assets.preparePackage("io.github.simonbas.screwbox.core.assets.internal");

        verify(log, never()).debug(anyString());
    }

    @Test
    void preparePackage_noAssetsLoaded_throwsException() {
        assets.preparePackage("io.github.simonbas.screwbox.core.assets.internal");

        assertThatThrownBy(() -> assets.preparePackage("de.suzufa.screwbox.core.assets.internal"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no assets found to prepare");

    }

    @Test
    void preparePackage_preparingPackageWithNonStaticAssets_noException() {
        assertThatNoException().isThrownBy(() -> assets.preparePackage("io.github.simonbas.screwbox.core"));
    }

    @AfterEach
    void afterEach() {
        ASSET_A.unload();
        ASSET_B.unload();
        ASSET_C.unload();
    }
}
