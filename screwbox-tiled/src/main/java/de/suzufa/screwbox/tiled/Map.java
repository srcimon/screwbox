package de.suzufa.screwbox.tiled;

import static java.util.Objects.requireNonNull;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.tiled.internal.DefaultProperties;
import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.LayerDictionaryBuilder;
import de.suzufa.screwbox.tiled.internal.TileDictionaryBuilder;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

/**
 * A Map created in Tiled-Editor.
 */
public class Map {

    private final MapEntity mapEntity;

    /**
     * Loads a new instance of a Tiled-Map that was saved as JSON-File.
     */
    public static Map fromJson(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("abc.xml is not a JSON-File");
        }

        final MapEntity entity = new JsonLoader().loadMap(fileName);
        return new Map(entity);
    }

    private Map(final MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public TileDicitonary buildTileDictionary() {
        return new TileDictionaryBuilder().buildDictionary(mapEntity);
    }

    public ObjectDictionary buildObjectDictionary() {
        return new ObjectDictionary(mapEntity);
    }

    public Bounds bounds() {
        final int width = mapEntity.getWidth() * mapEntity.getTilewidth();
        final int height = mapEntity.getHeight() * mapEntity.getTileheight();
        return Bounds.atOrigin(Vector.zero(), width, height);
    }

    public Properties properties() {
        return new DefaultProperties(mapEntity.getProperties());
    }

    public LayerDictionary buildLayerDictionary() {
        return new LayerDictionaryBuilder().buildDictionary(mapEntity);
    }

    @Override
    public String toString() {
        return "Map [width=" + bounds().width() + ",height=" + bounds().height() + "]";
    }

    public List<Layer> allLayers() {
        return buildLayerDictionary().allLayers();
    }

    public List<GameObject> allObjects() {
        return buildObjectDictionary().allObjects();
    }

    public List<Tile> allTiles() {
        return buildTileDictionary().allTiles();
    }

}
