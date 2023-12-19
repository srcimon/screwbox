package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssetTest {

    Asset<String> asset;

    @BeforeEach
    void beforeEach() {
        asset = Asset.asset(() -> "i like fish");
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
    void load_alreadyLoaded_doenstLoadAgain() {
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
}
