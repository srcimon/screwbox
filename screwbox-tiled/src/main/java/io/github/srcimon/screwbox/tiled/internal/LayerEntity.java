package io.github.srcimon.screwbox.tiled.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.emptyWhenNull;

public record LayerEntity(
        List<ChunkEntity> chunks,
        String compression,
        List<Integer> data,
        String draworder,
        @JsonProperty("class")
        String clazz,
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
        return emptyWhenNull(chunks);
    }

    public List<Integer> data() {
        return emptyWhenNull(data);
    }

    public List<LayerEntity> layers() {
        return emptyWhenNull(layers);
    }

    public List<ObjectEntity> objects() {
        return emptyWhenNull(objects);
    }

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }
}
