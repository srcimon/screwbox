package io.github.simonbas.screwbox.tiled.internal;

import java.util.List;

import static io.github.simonbas.screwbox.core.utils.ListUtil.emptyWhenNull;

public record ChunkEntity(List<Integer> data, int height, int width, int x, int y) {

    public List<Integer> data() {
        return emptyWhenNull(data);
    }
}