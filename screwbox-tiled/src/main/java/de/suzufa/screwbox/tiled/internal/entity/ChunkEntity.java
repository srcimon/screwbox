package de.suzufa.screwbox.tiled.internal.entity;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import java.util.List;

public record ChunkEntity(List<Integer> data, int height, int width, int x, int y) {

    public List<Integer> data() {
        return emptyWhenNull(data);
    }
}
