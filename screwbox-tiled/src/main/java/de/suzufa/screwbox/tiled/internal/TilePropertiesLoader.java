package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.TileEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

public class TilePropertiesLoader {

    private TilePropertiesLoader() {
        // hide constructor
    }

    public static PropertiesDictionary loadTileProperties(final MapEntity map) {
        final PropertiesDictionary dictionary = new PropertiesDictionary();
        for (final TilesetEntity tileset : map.getTilesets()) {
            for (final TileEntity tileEntity : tileset.getTiles()) {
                final Properties properties = new DefaultProperties(tileEntity.getProperties());
                dictionary.add(tileset.getFirstgid() + tileEntity.getId(), properties);
            }
        }
        return dictionary;
    }

}
