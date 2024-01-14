package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.core.Bounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapTest {

    private Map map;

    @BeforeEach
    void beforeEach() {
        map = Map.fromJson("underworld_map.json");
    }

    @Test
    void fromJson_fileNameIsNull_throwsException() {
        assertThatThrownBy(() -> Map.fromJson(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void fromJson_filenameDoenstEndWithJson_throwsException() {
        assertThatThrownBy(() -> Map.fromJson("Test.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file to be loaded is not a json: Test.txt");
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
    void layers_returnsMapLayers() {
        assertThat(map.layers()).hasSize(3)
                .anyMatch(layer -> layer.name().equals("objects"))
                .anyMatch(layer -> layer.order() == 0)
                .anyMatch(layer -> layer.order() == 1)
                .anyMatch(layer -> layer.order() == 2);
    }

    @Test
    void objects_returnsMapObjects() {
        assertThat(map.objects()).hasSize(7)
                .anyMatch(object -> object.name().equals("testimage"))
                .anyMatch(object -> object.name().equals("dummy"));
    }

    @Test
    void toString_returnsMapInformation() {
        assertThat(map).hasToString("Map [width=768.0,height=512.0]");
    }

    @Test
    void tiles_returnsMapTiles() {
        assertThat(map.tiles()).hasSize(41)
                .anyMatch(tile -> tile.layer().name().equals("world"));
    }

    @Test
    void objectWithName_objectMissing_isEmpty() {
        assertThat(map.objectWithName("unknown")).isEmpty();
    }

    @Test
    void objectWithName_objectFound_isObject() {
        assertThat(map.objectWithName("testpoint")).isPresent()
                .matches(object -> object.get().name().equals("testpoint"));
    }

    @Test
    void layerWithName_layerMissing_isEmpty() {
        assertThat(map.layerWithName("unknown")).isEmpty();
    }

    @Test
    void layerWithName_layerFound_isLayer() {
        assertThat(map.layerWithName("world")).isPresent()
                .matches(layer -> layer.get().name().equals("world"));
    }
}