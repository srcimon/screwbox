package de.suzufa.screwbox.tiled.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.suzufa.screwbox.core.utils.ResourceLoader;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectTemplateEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

public class JsonLoader {

    public MapEntity loadMap(final String fileName) {
        final File mapFile = ResourceLoader.resourceFile(fileName);

        final MapEntity map = deserialize(mapFile, MapEntity.class);
        final String directory = getDirectory(fileName);
        embedExternalTilesets(map, directory);
        embedObjectTemplates(map, directory);
        return map;
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
                        final File templateFile = ResourceLoader.resourceFile(directory + object.getTemplate());
                        final ObjectTemplateEntity objectTemplate = deserialize(templateFile,
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

    public TilesetEntity loadTileset(final String fileName) {
        final File tilesetFile = ResourceLoader.resourceFile(fileName);
        final TilesetEntity deserialize = deserialize(tilesetFile, TilesetEntity.class);
        final String fileNameInFileName = fileName.split("/")[fileName.split("/").length - 1];
        final String correctPath = fileName.replace(fileNameInFileName, deserialize.getImage());
        deserialize.setImage(correctPath);
        return deserialize;
    }

    private <T> T deserialize(final File mapFile, final Class<T> clazz) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(mapFile, clazz);
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be loaded: " + mapFile.getName(), e);
        }
    }

}
