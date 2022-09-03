package de.suzufa.screwbox.tiled;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.internal.MapEntity;
import de.suzufa.screwbox.tiled.internal.TilesetEntity;

class SpriteLoaderTest {

    @Test
    void loadTileset_externalTileset_returnsTileset() {
        MapEntity map = MapEntity.load("underworld_map.json");

        Tileset tileset = SpriteLoader.loadTileset(map);

        assertThat(tileset.spriteCount()).isEqualTo(48);
    }

    @Test
    void loadTileset_validFile_returnsTileset() {
        MapEntity map = MapEntity.load("map_animated_tileset.json");

        Tileset tileset = SpriteLoader.loadTileset(map);

        assertThat(tileset.spriteCount()).isEqualTo(16);
    }

    @Test
    void addTilesToDictionary_addsTiles() {
        TilesetEntity tileset = TilesetEntity.load("underworld.json");

        Tileset dictionary = new Tileset();
        SpriteLoader.addTilesToTileset(tileset, dictionary);

        assertThat(dictionary.spriteCount()).isEqualTo(32);
    }

    @Test
    void addTilesToDictionary_namePropertyPresent_addsNameIndex() {
        TilesetEntity tilesetEntity = TilesetEntity.load("underworld.json");
        Tileset dictionary = new Tileset();

        SpriteLoader.addTilesToTileset(tilesetEntity, dictionary);

        Sprite result = dictionary.findByName("myNamedSprite");

        assertThat(result).isNotNull();
    }

}
