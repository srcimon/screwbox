package de.suzufa.screwbox.tiled;

import de.suzufa.screwbox.tiled.internal.DefaultMap;
import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.SpriteLoader;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

//TODO: Map.fromJson()
//TODO: SpriteDictionary.fromJson
public final class TiledSupport {

    private TiledSupport() {
    }

    public static Map loadMap(final String fileName) {
        // TODO: only json support
        MapEntity map = new JsonLoader().loadMap(fileName);
        return new DefaultMap(map);
    }

    public static SpriteDictionary loadTileset(final String fileName) {
        final var tileset = new JsonLoader().loadTileset(fileName);
        final var dictionary = new SpriteDictionary();
        SpriteLoader.addTilesToDictionary(tileset, dictionary);
        return dictionary;
    }

}