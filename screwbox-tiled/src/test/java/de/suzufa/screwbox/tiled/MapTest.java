package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;

class MapTest {

    private Map map;

    @BeforeEach
    void beforeEach() {
        map = Map.fromJson("underworld_map.json");
    }

    @Test
    void bounds_returnsMapBounds() {
        assertThat(map.bounds()).isEqualTo(Bounds.atOrigin(0, 0, 768, 512));
    }

    @Test
    void properties_returnsMapProperties() {
        assertThat(map.properties().force("aMapProperty")).isEqualTo("aMapPropertyValue");
    }
}