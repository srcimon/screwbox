package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LayerTest {

    private Layer layer;

    @BeforeEach
    void beforeEach() {
        var map = Map.fromJson("underworld_map.json");
        layer = map.layerWithName("world").orElseThrow();
    }

    @Test
    void toString_containsName() {
        assertThat(layer).hasToString("Layer [name=world]");
    }

    @Test
    void opacity_fullOpacity_isMax() {
        assertThat(layer.opacity()).isEqualTo(Percent.max());
    }

    @Test
    void properties_noProperties_isEmpty() {
        assertThat(layer.properties().all()).isEmpty();
    }

    @Test
    void isImageLayer_tilelayer_isFalse() {
        assertThat(layer.isImageLayer()).isFalse();
    }

    @Test
    void parallaxX_parallaxIsZero_isOne() {
        assertThat(layer.parallaxX()).isOne();
    }

    @Test
    void parallaxY_parallaxIsZero_isOne() {
        assertThat(layer.parallaxY()).isOne();
    }
}
