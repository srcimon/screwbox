package de.suzufa.screwbox.core.assets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Asset<String> nullAfterLoadingAsset = Asset.asset((Supplier<String>) () -> null);

        assertThatThrownBy(() -> nullAfterLoadingAsset.load())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("asset null after loading");
    }

    @Test
    void isLoaded_notLoaded_isFalse() {
        assertThat(asset.isLoaded()).isFalse();
    }

    @Test
    void isLoaded_afterLoading_isTrue() {
        asset.load();

        assertThat(asset.isLoaded()).isTrue();
    }

    @Test
    void get_notLoaded_throwsException() {
        assertThatThrownBy(() -> asset.get())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("asset has not been loaded yet");
    }

    @Test
    void get_loaded_returnsValueFromSupplier() {
        asset.load();

        assertThat(asset.get()).isEqualTo("i like fish");
    }
}
