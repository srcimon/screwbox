package de.suzufa.screwbox.tiled.internal.entity;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

public record TileEntity(List<FrameEntity> animation, int id, String image, int imageheight, int imagewidth,
        LayerEntity objectgroup,
        double probability, List<PropertyEntity> properties, List<Integer> terrain, String type) {

    public List<PropertyEntity> properties() {
        return isNull(properties) ? new ArrayList<>() : properties;
    }

    public List<FrameEntity> animation() {
        return isNull(animation) ? new ArrayList<>() : animation;
    }

    public List<Integer> terrain() {
        return isNull(terrain) ? new ArrayList<>() : terrain;
    }
}
