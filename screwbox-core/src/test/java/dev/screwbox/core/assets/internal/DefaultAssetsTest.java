package dev.screwbox.core.assets.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.TestClasses;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.async.Async;
import dev.screwbox.core.async.internal.DefaultAsync;
import dev.screwbox.core.log.Log;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockitoSettings
class DefaultAssetsTest {

    @Mock
    Log log;

    ExecutorService executor;

    DefaultAssets assets;

    private static final Asset<String> ASSET_A = Asset.asset(() -> "loaded");
    private static final Asset<String> ASSET_B = Asset.asset(() -> "loaded");
    private static final Asset<String> ASSET_C = Asset.asset(() -> "loaded");

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor();
        Async async = new DefaultAsync(executor);
        assets = new DefaultAssets(async, log);
    }


    @Test
    void listAssetLocationsInPackage_packageContainsAssetBundles_listAssetBundles() {
        var locations = assets.listAssetLocationsInPackage("dev.screwbox.core.audio");

        assertThat(locations).hasSizeGreaterThan(5)
                .anyMatch(asset -> asset.id().equals("dev.screwbox.core.audio.SoundBundle.STEAM"));
    }

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
        var locations = assets.listAssetLocationsInPackage("dev.screwbox.core.assets.internal");

        assertThat(locations).hasSize(3)
                .allMatch(a -> a.loadingDuration().isEmpty())
                .anyMatch(a -> "dev.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "dev.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()))
                .anyMatch(a -> "dev.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_C".equals(a.id()));
    }

    @Test
    void preparePackage_assetsUnloadedEnabledLog_loadsAssetsAndLogs() {
        ASSET_C.load();

        assertThat(ASSET_A.isLoaded()).isFalse();
        assertThat(ASSET_B.isLoaded()).isFalse();

        assets.enableLogging();
        var loadedLocations = assets.preparePackage("dev.screwbox.core.assets.internal");

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();

        var logMessage = ArgumentCaptor.forClass(String.class);
        verify(log).debug(logMessage.capture());
        assertThat(logMessage.getValue()).startsWith("loaded 2 assets from package dev.screwbox.core.assets.internal in");

        assertThat(loadedLocations).hasSize(2)
                .allMatch(a -> a.loadingDuration().isPresent())
                .anyMatch(a -> "dev.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "dev.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()));
    }

    @Test
    void preparePackage_loggingDisabled_doesntLog() {
        assets.preparePackage("dev.screwbox.core.assets.internal");

        verify(log, never()).debug(anyString());
    }

    @Test
    void preparePackage_noAssetsLoaded_throwsException() {
        assertThatThrownBy(() -> assets.preparePackage("de.unknown"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no assets found to prepare");

    }

    @Test
    void preparePackage_preparingPackageWithNonStaticAssets_noException() {
        assertThatNoException().isThrownBy(() -> assets.preparePackage("dev.screwbox.core.assets"));
    }

    @Test
    void preparePackageAsync_afterAwaitingFinish_allAssetsArePrepared() {
        assets.preparePackageAsync("dev.screwbox.core.assets.internal");

        TestUtil.await(() -> !assets.isPreparing(), Duration.ofSeconds(2));

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();
        assertThat(ASSET_C.isLoaded()).isTrue();
    }

    @Test
    void isPreparing_afterStartingPreparation_isTrue() {
        assets.preparePackageAsync("dev.screwbox.core.assets.internal");

        assertThat(assets.isPreparing()).isTrue();
    }

    @Test
    void isPreparing_noPreparationInProgress_isFalse() {
        assertThat(assets.isPreparing()).isFalse();
    }

    @Test
    void disableLogging_loggingEnabled_disablesLogging() {
        assets.enableLogging();
        assets.disableLogging();

        assets.preparePackage("dev.screwbox.core.assets.internal");

        verify(log, never()).debug(anyString());
    }

    @Test
    void prepareClassPackage_assetBundleWhichIsNoEnum_throwsException() {
        assertThatThrownBy(() -> assets.prepareClassPackage(TestClasses.DefectAssetBundle.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("only enums are support to be asset bundles. class dev.screwbox.core.TestClasses$DefectAssetBundle is not an asset bundle");
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
        ASSET_A.unload();
        ASSET_B.unload();
        ASSET_C.unload();
    }
}
