package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.HasBounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.tiled.internal.MapEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * A Map created in Tiled-Editor. Used to import content created in Tiled-Editor in the game.
 *
 * @see Environment#importSource(ImportOptions)
 */
public class Map implements HasBounds {

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
        layers = new LayerCollection(mapEntity);
        tiles = new TileCollection(layers, mapEntity);
        objects = new GameObjectCollection(mapEntity);
        properties = new Properties(mapEntity.properties());
    }

    public List<Tile> tiles() {
        return tiles.all();
    }

    public List<GameObject> objects() {
        return objects.all();
    }

    public Bounds bounds() {
        final int width = mapEntity.width() * mapEntity.tilewidth();
        final int height = mapEntity.height() * mapEntity.tileheight();
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

    /**
     * Returns a set of all {@link Properties} within the map including the ones from {@link Layer layers},
     * {@link GameObject game objects}, {@link Tile tiles} and the properties of the {@link Map} itself.
     */
    public Set<Properties> allProperties() {
        final var allProperties = new HashSet<Properties>();
        allProperties.add(properties());
        tiles().forEach(tile -> allProperties.add(tile.properties()));
        layers().forEach(layer -> allProperties.add(layer.properties()));
        objects().forEach(object -> allProperties.add(object.properties()));
        return allProperties;
    }
}
