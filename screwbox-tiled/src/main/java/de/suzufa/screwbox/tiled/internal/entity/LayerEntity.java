package de.suzufa.screwbox.tiled.internal.entity;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

public record LayerEntity(
        List<ChunkEntity> chunks,
        String compression,
        List<Integer> data,
        String draworder,
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

    public List<ChunkEntity> chunks() {
        return isNull(chunks) ? new ArrayList<>() : chunks;
    }

    public List<Integer> data() {
        return isNull(data) ? new ArrayList<>() : data;
    }

    public List<LayerEntity> layers() {
        return isNull(layers) ? new ArrayList<>() : layers;
    }

    public List<ObjectEntity> objects() {
        return isNull(objects) ? new ArrayList<>() : objects;
    }

    public List<PropertyEntity> properties() {
        return isNull(properties) ? new ArrayList<>() : properties;
    }
}
