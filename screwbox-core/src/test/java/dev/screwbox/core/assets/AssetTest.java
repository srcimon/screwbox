package dev.screwbox.core.assets;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssetTest {

    static int counter;
    Asset<String> asset;
    ExecutorService executor;

    @BeforeEach
    void beforeEach() {
        executor = Executors.newCachedThreadPool();
        asset = Asset.asset(() -> "i like fish");
    }

    @Test
    void load_calledMultipleTimesInParallel_loadsOnlyOnce() {
        Asset<String> dontLoadMeTwice = Asset.asset(() -> {
            counter++;
            TestUtil.sleep(Duration.ofMillis(250));
            return "test";
        });
        executor.execute(dontLoadMeTwice::load);
        executor.execute(dontLoadMeTwice::load);
        executor.execute(dontLoadMeTwice::load);

        final var result = dontLoadMeTwice.get();
        assertThat(result).isEqualTo("test");
        assertThat(counter).isEqualTo(1);
    }

    @Test
    void asset_supplierNull_throwsException() {
        assertThatThrownBy(() -> Asset.asset((Supplier<String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("supplier must not be null");
    }

    @Test
    void load_supplierReturnsNull_throwsException() {
        Asset<String> nullAfterLoadingAsset = Asset.asset(() -> null);

        assertThatThrownBy(nullAfterLoadingAsset::load)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("asset is null after loading");
    }

    @Test
    void load_alreadyLoaded_doesntLoadAgain() {
        Asset<Time> timeAsset = Asset.asset(Time::now);
        asset.load();
        Time value = timeAsset.get();

        boolean wasLoaded = timeAsset.load();
        Time secondValue = timeAsset.get();

        assertThat(value).isEqualTo(secondValue);
        assertThat(wasLoaded).isFalse();
    }

    @Test
    void isLoaded_notLoaded_isFalse() {
        assertThat(asset.isLoaded()).isFalse();
    }

    @Test
    void isLoaded_afterLoading_isTrue() {
        boolean wasLoaded = asset.load();

        assertThat(asset.isLoaded()).isTrue();
        assertThat(wasLoaded).isTrue();
    }

    @Test
    void get_notLoaded_returnsValue() {
        assertThat(asset.get()).isEqualTo("i like fish");
    }

    @Test
    void get_loaded_returnsValue() {
        asset.load();

        assertThat(asset.get()).isEqualTo("i like fish");
    }

    @Test
    void unload_loaded_isNotLoaded() {
        asset.load();
        asset.unload();

        assertThat(asset.isLoaded()).isFalse();
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
