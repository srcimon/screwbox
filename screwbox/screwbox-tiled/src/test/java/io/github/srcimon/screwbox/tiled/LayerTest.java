package io.github.srcimon.screwbox.tiled;

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
}
