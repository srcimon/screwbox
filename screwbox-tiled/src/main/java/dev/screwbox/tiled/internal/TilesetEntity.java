package dev.screwbox.tiled.internal;

import dev.screwbox.core.utils.Json;

import java.util.List;

//TODO make record
public record TilesetEntity(

    String backgroundcolor,
    int columns,
    int firstgid,
    GridEntity grid,
    String image,
    int imageheight,
    int imagewidth,
    int margin,
    String name,
    String objectalignment,
    List<PropertyEntity> properties,
    String source,
    int spacing,
    List<TerrainEntity> terrains,
    int tilecount,
    String tiledversion,
    int tileheight,
    TileOffsetEntity tileoffset,
    List<TileEntity> tiles,
    int tilewidth,
    TransformationEntity transformations,
    String transparentcolor,
    String type,
    double version) {

    public static TilesetEntity load(final String fileName) {
        final TilesetEntity tilesetEntity = Json.loadFile(fileName, TilesetEntity.class);
        final String fileNameWithoudDirectories = fileName.split("/")[fileName.split("/").length - 1];
        final String correctPath = fileName.replace(fileNameWithoudDirectories, tilesetEntity.image());
        return tilesetEntity.replaceImage(correctPath);
    }

    public TilesetEntity replaceImage(String image) {
        return new TilesetEntity(
            backgroundcolor(),
            columns(),
            firstgid(),
            grid(),
            image,
            imageheight(),
            imagewidth(),
            margin(),
            name(),
            objectalignment(),
            properties(),
            source(),
            spacing(),
            terrains(),
            tilecount(),
            tiledversion(),
            tileheight(),
            tileoffset(),
            tiles(),
            tilewidth(),
            transformations(),
            transparentcolor(),
            type(),
            version());
    }

    public TilesetEntity replaceFirstGid(int firstGid) {
        return new TilesetEntity(
            backgroundcolor(),
            columns(),
            firstGid,
            grid(),
            image(),
            imageheight(),
            imagewidth(),
            margin(),
            name(),
            objectalignment(),
            properties(),
            source(),
            spacing(),
            terrains(),
            tilecount(),
            tiledversion(),
            tileheight(),
            tileoffset(),
            tiles(),
            tilewidth(),
            transformations(),
            transparentcolor(),
            type(),
            version());
    }

}
