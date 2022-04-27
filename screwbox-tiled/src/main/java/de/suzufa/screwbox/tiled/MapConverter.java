package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.tiled.internal.ConverterRegistry;

public class MapConverter {

    private final ConverterRegistry<GameObject> gameObjectConverters = new ConverterRegistry<>();
    private final ConverterRegistry<Tile> tileConverters = new ConverterRegistry<>();
    private final ConverterRegistry<Layer> layerConverters = new ConverterRegistry<>();
    private final ConverterRegistry<Map> mapConverters = new ConverterRegistry<>();

    public final void addObjectConverter(final Converter<GameObject> converter) {
        gameObjectConverters.register(converter);
    }

    public final void addLayerConverter(final Converter<Layer> converter) {
        layerConverters.register(converter);
    }

    public final void addTileConverter(final Converter<Tile> converter) {
        tileConverters.register(converter);
    }

    public final void addMapConverter(final Converter<Map> converter) {
        mapConverters.register(converter);
    }

    public List<Entity> createEnttiesFrom(final Map map) {
        List<Entity> allEntities = new ArrayList<>();
        allEntities.addAll(loadObjects(map));
        allEntities.addAll(loadTiles(map));
        allEntities.addAll(loadLayers(map));
        allEntities.addAll(loadMap(map));
        return allEntities;
    }

    private List<Entity> loadTiles(Map map) {
        List<Tile> allTiles = map.buildTileDictionary().allTiles();
        return tileConverters.load(allTiles);
    }

    private List<Entity> loadObjects(Map map) {
        List<GameObject> allObjects = map.buildObjectDictionary().allObjects();
        return gameObjectConverters.load(allObjects);
    }

    private List<Entity> loadLayers(Map map) {
        List<Layer> allLayers = map.buildLayerDictionary().allLayers();
        return layerConverters.load(allLayers);
    }

    private List<Entity> loadMap(Map map) {
        return mapConverters.load(map);
    }

}
