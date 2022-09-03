package de.suzufa.screwbox.tiled;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.SpriteLoader;

public class Tileset {

    private final Map<Integer, Sprite> spritesById = new HashMap<>();
    private final Map<String, Sprite> spritesByName = new HashMap<>();
    private final List<Sprite> allSprites = new ArrayList<>();

    public static Tileset fromJson(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("abc.xml is not a JSON-File");
        }
        final var tileset = new JsonLoader().loadTileset(fileName);
        final var dictionary = new Tileset();
        SpriteLoader.addTilesToTileset(tileset, dictionary);
        return dictionary;
    }

    public void addSprite(final int id, final Sprite sprite) {
        spritesById.put(id, sprite);
        allSprites.add(sprite);
    }

    public Sprite findById(final int id) {
        final Sprite sprite = spritesById.get(id);
        if (isNull(sprite)) {
            throw new IllegalArgumentException("sprite not found: " + id);
        }
        return sprite;
    }

    public int spriteCount() {
        return spritesById.size();
    }

    public Sprite findByName(final String name) {
        final Sprite sprite = spritesByName.get(name);
        if (isNull(sprite)) {
            throw new IllegalArgumentException("sprite not found: " + name);
        }
        return sprite;
    }

    public void addNameToSprite(final int id, final String name) {
        spritesByName.put(name, findById(id));
    }

    public List<Sprite> all() {
        return allSprites;
    }
}
