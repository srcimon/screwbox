package io.github.simonbas.screwbox.tiled.internal;

import java.util.List;

import static io.github.simonbas.screwbox.core.utils.ListUtil.emptyWhenNull;

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

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }

    public List<FrameEntity> animation() {
        return emptyWhenNull(animation);
    }

    public List<Integer> terrain() {
        return emptyWhenNull(terrain);
    }
}
