package dev.screwbox.playground.map;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

public class SpawnArea extends Floor {

    private final Color team;

    public SpawnArea(Color team) {
        this.team = team;
    }

    @Override
    public Entity assembleFrom(TileMap.Tile<Color> tile) {
        return super.assembleFrom(tile)
            .add(new SpawnComponent(), config -> config.team = team);
    }
}
