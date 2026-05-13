package dev.screwbox.tiled.internal;

import dev.screwbox.core.utils.Json;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public record MapEntity(
    String backgroundColor,
    List<PropertyEntity> properties,
    int compressionlevel,
    int height,
    int hexsidelength,
    boolean infinite,
    int nextlayerid,
    int nextobjectid,
    String orientation,
    String renderorder,
    String staggeraxis,
    String staggerindex,
    String tiledversion,
    int tilewidth,
    int tileheight,
    String type,
    float version,
    int width,
    List<LayerEntity> layers,
    List<TilesetEntity> tilesets
) {

    public static MapEntity load(final String fileName) {
        final MapEntity map = Json.loadFile(fileName, MapEntity.class);
        final String directory = getDirectory(fileName);
        map.embedExternalTilesets(directory);
        map.embedObjectTemplates(directory);
        return map;
    }

    private static String getDirectory(final String fileName) {
        final String[] parts = fileName.split("/");
        final String file = parts[parts.length - 1];
        return fileName.replace(file, "");
    }

    private void embedObjectTemplates(final String directory) {
        for (final LayerEntity layer : layers()) {
            for (int i = 0; i < layer.objects().size(); i++) {
                final ObjectEntity object = layer.objects().get(i);
                if (nonNull(object.template())) {
                    final var replacement = Json.loadFile(directory + object.template(), ObjectTemplateEntity.class)
                        .object();

                    final boolean hasSize = object.width() != 0 && object.height() != 0;
                    final var replacementEntity = new ObjectEntity(
                        replacement.ellipse(),
                        replacement.gid(),
                        hasSize ? object.height() : replacement.height(),
                        object.id(),
                        replacement.name(),
                        replacement.point(),
                        replacement.polygon(),
                        replacement.polyline(),
                        object.properties(),
                        replacement.rotation(),
                        replacement.template(),
                        replacement.text(),
                        replacement.type(),
                        replacement.visible(),
                        hasSize ? object.width() : replacement.width(),
                        object.x(),
                        object.y()
                    );

                    layer.objects().set(i, replacementEntity);
                }
            }
        }
    }

    private void embedExternalTilesets(final String directory) {
        final List<TilesetEntity> fullTilesets = new ArrayList<>();
        for (final TilesetEntity tileset : tilesets()) {
            if (tileset.source() == null) {
                fullTilesets.add(tileset);
            } else {
                final TilesetEntity externalTileset = TilesetEntity.load(directory + tileset.source());
                fullTilesets.add(externalTileset.replaceFirstGid(tileset.firstgid()));
            }
        }
        tilesets.clear();
        tilesets.addAll(fullTilesets);
    }
}
