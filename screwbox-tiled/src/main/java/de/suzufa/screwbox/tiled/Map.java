package de.suzufa.screwbox.tiled;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.tiled.internal.DefaultProperties;
import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.LayerDictionaryBuilder;
import de.suzufa.screwbox.tiled.internal.ObjectDictionaryBuilder;
import de.suzufa.screwbox.tiled.internal.TileDictionaryBuilder;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class Map {

    public static Map fromJson(final String fileName) {
        // TODO: only json support
        final MapEntity map = new JsonLoader().loadMap(fileName);
        return new Map(map);
    }

    private final MapEntity mapEntity;

    private Map(final MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public TileDicitonary buildTileDictionary() {
        return new TileDictionaryBuilder().buildDictionary(mapEntity);
    }

    public ObjectDictionary buildObjectDictionary() {
        return new ObjectDictionaryBuilder().buildDictionary(mapEntity);
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
