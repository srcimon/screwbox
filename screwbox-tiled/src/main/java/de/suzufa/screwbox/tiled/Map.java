package de.suzufa.screwbox.tiled;

import static java.util.Objects.requireNonNull;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.tiled.internal.JsonLoader;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

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

    public TileDicitonary tiles() {
        return new TileDicitonary(mapEntity);
    }

    public GameObjectsCollection objects() {
        return new GameObjectsCollection(mapEntity);
    }

    public Bounds bounds() {
        final int width = mapEntity.getWidth() * mapEntity.getTilewidth();
        final int height = mapEntity.getHeight() * mapEntity.getTileheight();
        return Bounds.atOrigin(Vector.zero(), width, height);
    }

    public Properties properties() {
        return new Properties(mapEntity.getProperties());
    }

    public LayersCollection layers() {
        return new LayersCollection(mapEntity);
    }

    @Override
    public String toString() {
        return "Map [width=" + bounds().width() + ",height=" + bounds().height() + "]";
    }

}
