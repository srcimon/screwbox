package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TileDicitonary;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class TileDictionaryBuilder {

    public TileDicitonary buildDictionary(MapEntity map) {
        SpriteDictionary spriteDictionary = SpriteLoader.loadSprites(map);
        TileDicitonary tileDictionary = new DefaultTileDictionary();
        PropertiesDictionary propertiesDictionary = TilePropertiesLoader.loadTileProperties(map);

        int order = 0;
        for (LayerEntity layerEntity : map.getLayers()) {
            for (int y = 0; y < layerEntity.getHeight(); y++) {
                for (int x = 0; x < layerEntity.getWidth(); x++) {
                    Integer tileId = layerEntity.getData().get(y * layerEntity.getWidth() + x);
                    if (tileId != 0) {
                        double width = map.getTilewidth();
                        double height = map.getTileheight();
                        double offsetX = x * width;
                        double offsetY = y * height;
                        Bounds bounds = Bounds.atOrigin(offsetX, offsetY, width, height);
                        Sprite sprite = spriteDictionary.findById(tileId);
                        Layer layer = new DefaultLayer(layerEntity, order);
                        Properties properties = propertiesDictionary.get(tileId);
                        var tile = new DefaultTile(sprite, bounds, layer, properties);
                        tileDictionary.add(tile);
                    }
                }
            }
            order++;
        }
        return tileDictionary;
    }

}
