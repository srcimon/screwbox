package dev.screwbox.tiled.internal;

import java.util.List;

public record WangSetEntity(
    List<WangColorEntity> colors,
    String name,
    List<PropertyEntity> properties,
    int tile,
    String type,
    List<WangTileEntity> wangtiles) {

}
