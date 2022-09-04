package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
