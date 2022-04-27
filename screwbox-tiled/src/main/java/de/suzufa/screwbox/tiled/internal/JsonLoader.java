package de.suzufa.screwbox.tiled.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.suzufa.screwbox.core.utils.ResourceLoader;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectTemplateEntity;
import de.suzufa.screwbox.tiled.internal.entity.TilesetEntity;

public class JsonLoader {

    public MapEntity loadMap(String fileName) {
        Objects.requireNonNull(fileName, "fileName must not be null");
        File mapFile = ResourceLoader.resourceFile(fileName);

        MapEntity map = deserialize(mapFile, MapEntity.class);
        embedExternalTilesets(map, getDirectory(fileName));
        embedObjectTemplates(map, getDirectory(fileName));
        return map;
    }

    private String getDirectory(String fileName) {
        String[] parts = fileName.split("/");
        String file = parts[parts.length - 1];
        return fileName.replace(file, "");
    }

    private void embedObjectTemplates(MapEntity map, String directory) {
        for (LayerEntity layer : map.getLayers()) {
            if (layer.getObjects() != null) {
                for (int i = 0; i < layer.getObjects().size(); i++) {
                    ObjectEntity object = layer.getObjects().get(i);
                    if (object.getTemplate() != null) {
                        File templateFile = ResourceLoader.resourceFile(directory + object.getTemplate());
                        ObjectTemplateEntity objectTemplate = deserialize(templateFile, ObjectTemplateEntity.class);
                        ObjectEntity replacement = objectTemplate.getObject();
                        replacement.setId(object.getId());
                        replacement.setX(object.getX());
                        replacement.setY(object.getY());
                        if (object.getWidth() != 0 && object.getHeight() != 0) {
                            replacement.setWidth(object.getWidth());
                            replacement.setHeight(object.getHeight());
                        }

                        replacement.setProperties(object.getProperties());// TODO: combine neccessary?
                        layer.getObjects().set(i, replacement);
                    }
                }
            }
        }

    }

    private void embedExternalTilesets(MapEntity map, String directory) {
        List<TilesetEntity> fullTilesets = new ArrayList<>();
        for (TilesetEntity tileset : map.getTilesets()) {
            if (tileset.getSource() == null) {
                fullTilesets.add(tileset);
            } else {
                TilesetEntity externalTileset = loadTileset(directory + tileset.getSource());
                externalTileset.setFirstgid(tileset.getFirstgid());
                fullTilesets.add(externalTileset);
            }
        }
        map.setTilesets(fullTilesets);
    }

    public TilesetEntity loadTileset(String fileName) {
        Objects.requireNonNull(fileName, "fileName must not be null");
        File tilesetFile = ResourceLoader.resourceFile(fileName);
        TilesetEntity deserialize = deserialize(tilesetFile, TilesetEntity.class);
        String fileNameInFileName = fileName.split("/")[fileName.split("/").length - 1];
        String correctPath = fileName.replace(fileNameInFileName, deserialize.getImage());
        deserialize.setImage(correctPath);
        return deserialize;
    }

    private <T> T deserialize(File mapFile, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(mapFile, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("file could not be loaded: " + mapFile.getName(), e);
        }
    }

}
