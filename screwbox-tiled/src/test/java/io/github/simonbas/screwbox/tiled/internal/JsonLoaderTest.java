//TODO: REPLACE
//package de.suzufa.screwbox.tiled.internal;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
//
//class JsonLoaderTest {
//
//    private JsonLoader jsonLoader;
//
//    @BeforeEach
//    void beforeEach() {
//        jsonLoader = new JsonLoader();
//    }
//
//    @Test
//    void loadMap_fileIsNull_throwsException() {
//        assertThatThrownBy(() -> jsonLoader.loadMap(null))
//                .isInstanceOf(NullPointerException.class)
//                .hasMessage("fileName must not be null");
//    }
//
//    @Test
//    void loadMap_mapIsValid_returnsMap() {
//        MapEntity map = jsonLoader.loadMap("underworld_map.json");
//
//        assertThat(map.getLayers()).hasSize(3);
//        assertThat(map.getTilesets()).hasSize(2);
//    }
//
//    @Test
//    void loadMap_externalizedTilemap_returnsMapWithEmbeddedTilemap() {
//        MapEntity map = jsonLoader.loadMap("underworld_map.json");
//
//        assertThat(map.getTilesets().get(0).getImage()).isEqualTo("underworld.png");
//        assertThat(map.getTilesets().get(1).getImage()).isEqualTo("second_tileset.png");
//    }
//}
