package de.suzufa.screwbox.tiled;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.internal.LayerEntity;
import de.suzufa.screwbox.tiled.internal.MapEntity;
import de.suzufa.screwbox.tiled.internal.TileEntity;
import de.suzufa.screwbox.tiled.internal.TilesetEntity;

public class TileCollection {

    private final List<Tile> tiles = new ArrayList<>();

    TileCollection(final MapEntity map) {
        final Tileset tileset = SpriteLoader.loadTileset(map);
        var propertiesHolder = loadTileProperties(map);

        int order = 0;
        for (final LayerEntity layerEntity : map.getLayers()) {
            for (int y = 0; y < layerEntity.height(); y++) {
                for (int x = 0; x < layerEntity.width(); x++) {
                    final Integer tileId = layerEntity.data().get(y * layerEntity.width() + x);
                    if (tileId != 0) {
                        final double width = map.getTilewidth();
                        final double height = map.getTileheight();
                        final double offsetX = x * width;
                        final double offsetY = y * height;
                        final Bounds bounds = Bounds.atOrigin(offsetX, offsetY, width, height);
                        final Sprite sprite = tileset.findById(tileId);
                        final Layer layer = new Layer(layerEntity, order);
                        final Properties properties = propertiesHolder.get(tileId);
                        final var tile = new Tile(sprite, bounds, layer,
                                properties == null ? new Properties(emptyList()) : properties);
                        add(tile);
                    }
                }
            }
            order++;
        }
    }

    void add(final Tile tile) {
        tiles.add(tile);
    }

    public List<Tile> all() {
        return tiles;
    }

    private HashMap<Integer, Properties> loadTileProperties(final MapEntity map) {
        var propertiesHolder = new HashMap<Integer, Properties>();
        for (final TilesetEntity tileset : map.getTilesets()) {
            for (final TileEntity tileEntity : tileset.getTiles()) {
                final Properties properties = new Properties(tileEntity.properties());
                propertiesHolder.put(tileset.getFirstgid() + tileEntity.id(), properties);
            }
        }
        return propertiesHolder;
    }

}