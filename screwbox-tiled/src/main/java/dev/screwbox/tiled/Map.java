package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.tiled.internal.MapEntity;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

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
        if (!requireNonNull(fileName, "fileName must not be null").toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("file to be loaded is not a json: " + fileName);
        }
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
