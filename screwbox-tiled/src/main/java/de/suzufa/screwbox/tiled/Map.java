package de.suzufa.screwbox.tiled;

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

    /**
     * Loads a new instance of a Tiled-Map that was saved as JSON-File.
     */
    public static Map fromJson(final String fileName) {
        final MapEntity entity = MapEntity.load(fileName);
        return new Map(entity);
    }

    private Map(final MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    public TileCollection tiles() {
        return new TileCollection(mapEntity);
    }

    public GameObjectCollection objects() {
        return new GameObjectCollection(mapEntity);
    }

    public Bounds bounds() {
        final int width = mapEntity.getWidth() * mapEntity.getTilewidth();
        final int height = mapEntity.getHeight() * mapEntity.getTileheight();
        return Bounds.atOrigin(Vector.zero(), width, height);
    }

    public Properties properties() {
        return new Properties(mapEntity.getProperties());
    }

    public LayerCollection layers() {
        return new LayerCollection(mapEntity);
    }

    @Override
    public String toString() {
        return "Map [width=" + bounds().width() + ",height=" + bounds().height() + "]";
    }

}
