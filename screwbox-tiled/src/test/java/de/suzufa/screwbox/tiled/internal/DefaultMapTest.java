package de.suzufa.screwbox.tiled.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.TiledSupport;

class DefaultMapTest {

    private Map map;

    @BeforeEach
    void beforeEach() {
        map = TiledSupport.loadMap("underworld_map.json");
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
