package de.suzufa.screwbox.core.assets;

import static de.suzufa.screwbox.core.Time.now;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetLocationTest {

    static final Asset<String> TEST_ASSET = Asset.asset(() -> "hello world");

    private AssetLocation assetLocation;

    @BeforeEach
    void beforeEach() throws Exception {
        Field field = AssetLocationTest.class.getDeclaredField("TEST_ASSET");
        assetLocation = AssetLocation.tryToCreateAt(field).get();
        assetLocation.load();
    }

    @Test
    void loadingTime_assetLoaded_isTimeBeforeNow() {
        assertThat(assetLocation.loadingTime().isBefore(now())).isTrue();
    }

    @Test
    void loadingDuration_assetLoaded_isPositive() {
        assertThat(assetLocation.loadingDuration().nanos()).isPositive();
    }
}
