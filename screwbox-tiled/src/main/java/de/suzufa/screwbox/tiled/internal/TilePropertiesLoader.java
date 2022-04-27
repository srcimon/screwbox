package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.TileEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

public class TilePropertiesLoader {

    public static PropertiesDictionary loadTileProperties(MapEntity map) {
        PropertiesDictionary dictionary = new PropertiesDictionary();
        for (TilesetEntity tileset : map.getTilesets()) {
            for (TileEntity tileEntity : tileset.getTiles()) {
                Properties properties = new DefaultProperties(tileEntity.getProperties());
                dictionary.add(tileset.getFirstgid() + tileEntity.getId(), properties);
            }
        }
        return dictionary;
    }

}
