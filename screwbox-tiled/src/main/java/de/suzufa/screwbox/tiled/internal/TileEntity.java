package de.suzufa.screwbox.tiled.internal;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

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
