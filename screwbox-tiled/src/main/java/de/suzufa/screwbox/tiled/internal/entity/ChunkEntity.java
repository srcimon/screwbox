package de.suzufa.screwbox.tiled.internal.entity;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

public record ChunkEntity(List<Integer> data, int height, int width, int x, int y) {

    public List<Integer> data() {
        return isNull(data) ? new ArrayList<>() : data;
    }
}
