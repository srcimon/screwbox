package dev.screwbox.tiled.internal;

import java.util.List;

public record TerrainEntity(String name, List<PropertyEntity> properties, int tile) {

}
