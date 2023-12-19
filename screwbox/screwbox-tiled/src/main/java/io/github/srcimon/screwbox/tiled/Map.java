package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.tiled.internal.MapEntity;

import java.util.List;
import java.util.Optional;

/**
 * A Map created in Tiled-Editor. Used to import content created in Tiled-Editor
 * in your game.
 *
 * @see Environment#importSource(Object)
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

    public Optional<GameObject> objectWithName(final String name) {
        return objects.findByName(name);
    }

    public Optional<Layer> layerWithName(final String name) {
        return layers.findByName(name);
    }

}
