package de.suzufa.screwbox.tiled.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.tiled.Map;

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

    @Test
    void fromJson_fileNameIsNull_throwsException() {
        assertThatThrownBy(() -> Map.fromJson(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void fromJson_notAJsonFile_throwsException() {
        assertThatThrownBy(() -> Map.fromJson("abc.xml"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("abc.xml is not a JSON-File");
    }
}
