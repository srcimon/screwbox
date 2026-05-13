package dev.screwbox.tiled.internal;

import java.util.List;

public record TileEntity(
    List<FrameEntity> animation,
    int id,
    String image,
    int imageheight,
    int imagewidth,
    LayerEntity objectgroup,
    double probability,
    List<PropertyEntity> properties,
    List<Integer> terrain,
    String type) {

}
