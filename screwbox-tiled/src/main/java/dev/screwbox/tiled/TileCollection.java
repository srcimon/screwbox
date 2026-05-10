package dev.screwbox.tiled;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.tiled.internal.LayerEntity;
import dev.screwbox.tiled.internal.MapEntity;
import dev.screwbox.tiled.internal.TileEntity;
import dev.screwbox.tiled.internal.TilesetEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyList;

class TileCollection {

    private final List<Tile> tiles = new ArrayList<>();

    TileCollection(final MapEntity map) {
        final Tileset tileset = new Tileset(map.tilesets());
        var propertiesHolder = loadTileProperties(map);

        int order = 0;
        for (final LayerEntity layerEntity : map.layers()) {
            for (int y = 0; y < layerEntity.height(); y++) {
                for (int x = 0; x < layerEntity.width(); x++) {
                    final Integer tileId = layerEntity.data().get(y * layerEntity.width() + x);
                    if (tileId != 0) {
                        final double width = map.tilewidth();
                        final double height = map.tileheight();
                        final double offsetX = x * width + layerEntity.offsetx();
                        final double offsetY = y * height + layerEntity.offsety();
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

    List<Tile> all() {
        return tiles;
    }

    private HashMap<Integer, Properties> loadTileProperties(final MapEntity map) {
        var propertiesHolder = new HashMap<Integer, Properties>();
        for (final TilesetEntity tileset : map.tilesets()) {
            for (final TileEntity tileEntity : tileset.tiles()) {
                final Properties properties = new Properties(tileEntity.properties());
                propertiesHolder.put(tileset.firstgid() + tileEntity.id(), properties);
            }
        }
        return propertiesHolder;
    }
}