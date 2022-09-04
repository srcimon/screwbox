package de.suzufa.screwbox.tiled;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.tiled.internal.MapEntity;

/**
 * A Map created in Tiled-Editor. Used to import content created in Tiled-Editor
 * in your game.
 * 
 * @see EntityEngine#importSource(Object)
 */
public class Map {

    private final MapEntity mapEntity;
    private final TileCollection tiles;
    private final GameObjectCollection objects;
    private final Properties properties;
    private final LayerCollection layers;

    /**
     * Loads a new instance of a Tiled-Map that was saved as JSON-File.
     */
    public static Map fromJson(final String fileName) {
        final MapEntity entity = MapEntity.load(fileName);
        return new Map(entity);
    }

    private Map(final MapEntity mapEntity) {
        this.mapEntity = mapEntity;
        tiles = new TileCollection(mapEntity);
        objects = new GameObjectCollection(mapEntity);
        properties = new Properties(mapEntity.getProperties());
        layers = new LayerCollection(mapEntity);
    }

    public List<Tile> tiles() {
        return tiles.all();
    }

    public List<GameObject> objects() {
        return objects.all();
    }

    public Bounds bounds() {
        final int width = mapEntity.getWidth() * mapEntity.getTilewidth();
        final int height = mapEntity.getHeight() * mapEntity.getTileheight();
        return Bounds.atOrigin(Vector.zero(), width, height);
    }

    public Properties properties() {
        return properties;
    }

    public List<Layer> layers() {
        return layers.all();
    }

    @Override
    public String toString() {
        return "Map [width=" + bounds().width() + ",height=" + bounds().height() + "]";
    }

}
