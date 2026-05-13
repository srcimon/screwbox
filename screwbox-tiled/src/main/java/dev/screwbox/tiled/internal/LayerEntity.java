package dev.screwbox.tiled.internal;

import java.util.List;

public record LayerEntity(
    List<ChunkEntity> chunks,
    String compression,
    List<Integer> data,
    String draworder,
    String class_,
    String encoding,
    int height,
    int id,
    String image,
    double parallaxx,
    double parallaxy,
    List<LayerEntity> layers,
    String name,
    List<ObjectEntity> objects,
    double offsetx,
    double offsety,
    double opacity,
    List<PropertyEntity> properties,
    int startx,
    int starty,
    String tintcolor,
    String transparentcolor,
    String type,
    boolean visible,
    int width,
    int x,
    int y) {
}
