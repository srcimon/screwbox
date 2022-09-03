package de.suzufa.screwbox.tiled.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Tileset;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

class SpriteLoaderTest {

    @Test
    void loadSprites_externalTileset_returnsDictionary() {
        MapEntity map = new JsonLoader().loadMap("underworld_map.json");

        Tileset spriteDictionary = SpriteLoader.loadTileset(map);

        assertThat(spriteDictionary.spriteCount()).isEqualTo(48);
    }

    @Test
    void loadSprites_validFile_returnsDictionary() {
        MapEntity map = new JsonLoader().loadMap("map_animated_tileset.json");

        Tileset spriteDictionary = SpriteLoader.loadTileset(map);

        assertThat(spriteDictionary.spriteCount()).isEqualTo(16);
    }

    @Test
    void addTilesToDictionary_addsTiles() {
        TilesetEntity tileset = new JsonLoader().loadTileset("underworld.json");

        Tileset dictionary = new Tileset();
        SpriteLoader.addTilesToTileset(tileset, dictionary);

        assertThat(dictionary.spriteCount()).isEqualTo(32);
    }

    @Test
    void addTilesToDictionary_namePropertyPresent_addsNameIndex() {
        TilesetEntity tilesetEntity = new JsonLoader().loadTileset("underworld.json");
        Tileset dictionary = new Tileset();

        SpriteLoader.addTilesToTileset(tilesetEntity, dictionary);

        Sprite result = dictionary.findByName("myNamedSprite");

        assertThat(result).isNotNull();
    }

}
