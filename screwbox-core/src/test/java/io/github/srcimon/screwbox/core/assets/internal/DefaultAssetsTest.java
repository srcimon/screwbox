package io.github.srcimon.screwbox.core.assets.internal;

import io.github.srcimon.screwbox.core.DefectAssetBundle;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.async.Async;
import io.github.srcimon.screwbox.core.async.internal.DefaultAsync;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        var locations = assets.listAssetLocationsInPackage("io.github.srcimon.screwbox.core.audio");

        assertThat(locations).hasSizeGreaterThan(5)
                .anyMatch(asset -> asset.id().equals("io.github.srcimon.screwbox.core.audio.SoundBundle.STEAM"));
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
        var locations = assets.listAssetLocationsInPackage("io.github.srcimon.screwbox.core.assets.internal");

        assertThat(locations).hasSize(3)
                .allMatch(a -> a.loadingDuration().isEmpty())
                .anyMatch(a -> "io.github.srcimon.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "io.github.srcimon.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()))
                .anyMatch(a -> "io.github.srcimon.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_C".equals(a.id()));
    }

    @Test
    void preparePackage_assetsUnloadedEnabledLog_loadsAssetsAndLogs() {
        ASSET_C.load();

        assertThat(ASSET_A.isLoaded()).isFalse();
        assertThat(ASSET_B.isLoaded()).isFalse();

        assets.enableLogging();
        var loadedLocations = assets.preparePackage("io.github.srcimon.screwbox.core.assets.internal");

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();

        var logMessage = ArgumentCaptor.forClass(String.class);
        verify(log).debug(logMessage.capture());
        assertThat(logMessage.getValue()).startsWith("loaded 2 assets in ");

        assertThat(loadedLocations).hasSize(2)
                .allMatch(a -> a.loadingDuration().isPresent())
                .anyMatch(a -> "io.github.srcimon.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_A".equals(a.id()))
                .anyMatch(a -> "io.github.srcimon.screwbox.core.assets.internal.DefaultAssetsTest.ASSET_B".equals(a.id()));
    }

    @Test
    void preparePackage_loggingDisabled_doesntLog() {
        assets.preparePackage("io.github.srcimon.screwbox.core.assets.internal");

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
        assertThatNoException().isThrownBy(() -> assets.preparePackage("io.github.srcimon.screwbox.core.assets"));
    }

    @Test
    void preparePackageAsync_afterAwaitingFinish_allAssetsArePrepared() {
        assets.preparePackageAsync("io.github.srcimon.screwbox.core.assets.internal");

        TestUtil.await(() -> !assets.isPreparing(), Duration.ofSeconds(2));

        assertThat(ASSET_A.isLoaded()).isTrue();
        assertThat(ASSET_B.isLoaded()).isTrue();
        assertThat(ASSET_C.isLoaded()).isTrue();
    }

    @Test
    void isPreparing_afterStartingPreperation_isTrue() {
        assets.preparePackageAsync("io.github.srcimon.screwbox.core.assets.internal");

        assertThat(assets.isPreparing()).isTrue();
    }

    @Test
    void isPreparing_noPreperationInProgress_isFalse() {
        assertThat(assets.isPreparing()).isFalse();
    }

    @Test
    void disableLogging_loggingEnabled_disablesLogging() {
        assets.enableLogging();
        assets.disableLogging();

        assets.preparePackage("io.github.srcimon.screwbox.core.assets.internal");

        verify(log, never()).debug(anyString());
    }

    @Test
    void prepareClassPackage_assetBundleWhichIsNoEnum_throwsException() {
        assertThatThrownBy(() -> assets.prepareClassPackage(DefectAssetBundle.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("only enums are support to be asset bundles. class io.github.srcimon.screwbox.core.DefectAssetBundle is not an asset bundle");
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
        ASSET_A.unload();
        ASSET_B.unload();
        ASSET_C.unload();
    }
}
