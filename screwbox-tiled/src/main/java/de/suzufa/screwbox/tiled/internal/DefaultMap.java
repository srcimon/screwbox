package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.tiled.LayerDictionary;
import de.suzufa.screwbox.tiled.Map;
import de.suzufa.screwbox.tiled.ObjectDictionary;
import de.suzufa.screwbox.tiled.Properties;
import de.suzufa.screwbox.tiled.TileDicitonary;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class DefaultMap implements Map {

    private final MapEntity mapEntity;

    public DefaultMap(final MapEntity mapEntity) {
        this.mapEntity = mapEntity;
    }

    @Override
    public TileDicitonary buildTileDictionary() {
        return new TileDictionaryBuilder().buildDictionary(mapEntity);
    }

    @Override
    public ObjectDictionary buildObjectDictionary() {
        return new ObjectDictionaryBuilder().buildDictionary(mapEntity);
    }

    @Override
    public Bounds bounds() {
        int width = mapEntity.getWidth() * mapEntity.getTilewidth();
        int height = mapEntity.getHeight() * mapEntity.getTileheight();
        return Bounds.atOrigin(Vector.zero(), width, height);
    }

    @Override
    public Properties properties() {
        return new DefaultProperties(mapEntity.getProperties());
    }

    @Override
    public LayerDictionary buildLayerDictionary() {
        return new LayerDictionaryBuilder().buildDictionary(mapEntity);
    }

    @Override
    public String toString() {
        return "Map [width=" + bounds().width() + ",height=" + bounds().height() + "]";
    }

}
