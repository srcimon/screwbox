package de.suzufa.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.utils.Resources;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectTemplateEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

@Deprecated
//TODO: get rid of that class
public class JsonLoader {

    public MapEntity loadMap(final String fileName) {
        final MapEntity map = Resources.loadJson(fileName, MapEntity.class);
        final String directory = getDirectory(fileName);
        embedExternalTilesets(map, directory);
        embedObjectTemplates(map, directory);
        return map;
    }

    public TilesetEntity loadTileset(final String fileName) {
        final TilesetEntity tilesetEntity = Resources.loadJson(fileName, TilesetEntity.class);
        final String fileNameWithoudDirectories = fileName.split("/")[fileName.split("/").length - 1];
        final String correctPath = fileName.replace(fileNameWithoudDirectories, tilesetEntity.getImage());
        tilesetEntity.setImage(correctPath);
        return tilesetEntity;
    }

    private String getDirectory(final String fileName) {
        final String[] parts = fileName.split("/");
        final String file = parts[parts.length - 1];
        return fileName.replace(file, "");
    }

    private void embedObjectTemplates(final MapEntity map, final String directory) {
        for (final LayerEntity layer : map.getLayers()) {
            if (layer.objects() != null) {
                for (int i = 0; i < layer.objects().size(); i++) {
                    final ObjectEntity object = layer.objects().get(i);
                    if (object.getTemplate() != null) {
                        final ObjectTemplateEntity objectTemplate = Resources.loadJson(
                                directory + object.getTemplate(),
                                ObjectTemplateEntity.class);
                        final ObjectEntity replacement = objectTemplate.object();
                        replacement.setId(object.getId());
                        replacement.setX(object.getX());
                        replacement.setY(object.getY());
                        if (object.getWidth() != 0 && object.getHeight() != 0) {
                            replacement.setWidth(object.getWidth());
                            replacement.setHeight(object.getHeight());
                        }

                        replacement.setProperties(object.getProperties());// TODO: combine neccessary?
                        layer.objects().set(i, replacement);
                    }
                }
            }
        }

    }

    private void embedExternalTilesets(final MapEntity map, final String directory) {
        final List<TilesetEntity> fullTilesets = new ArrayList<>();
        for (final TilesetEntity tileset : map.getTilesets()) {
            if (tileset.getSource() == null) {
                fullTilesets.add(tileset);
            } else {
                final TilesetEntity externalTileset = loadTileset(directory + tileset.getSource());
                externalTileset.setFirstgid(tileset.getFirstgid());
                fullTilesets.add(externalTileset);
            }
        }
        map.setTilesets(fullTilesets);
    }
}