package de.suzufa.screwbox.tiled.internal;

import static de.suzufa.screwbox.core.utils.ListUtil.emptyWhenNull;

import java.util.List;

public record TerrainEntity(String name, List<PropertyEntity> properties, int tile) {

    public List<PropertyEntity> properties() {
        return emptyWhenNull(properties);
    }
}
