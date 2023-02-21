package io.github.simonbas.screwbox.tiled.internal;

import java.util.List;

import static io.github.simonbas.screwbox.core.utils.ListUtil.emptyWhenNull;

public record TerrainEntity(String name, List<PropertyEntity> properties, int tile) {

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }
}
