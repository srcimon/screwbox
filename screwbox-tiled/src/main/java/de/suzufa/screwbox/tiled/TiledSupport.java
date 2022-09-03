package de.suzufa.screwbox.tiled;

import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.SpriteLoader;

//TODO: SpriteDictionary.fromJson
@Deprecated
public final class TiledSupport {

    private TiledSupport() {
    }

    public static SpriteDictionary loadTileset(final String fileName) {
        final var tileset = new JsonLoader().loadTileset(fileName);
        final var dictionary = new SpriteDictionary();
        SpriteLoader.addTilesToDictionary(tileset, dictionary);
        return dictionary;
    }

}