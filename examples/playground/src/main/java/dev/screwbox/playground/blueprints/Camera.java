package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.blueprints.Blueprint;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.utils.TileMap;

public class Camera implements Blueprint<TileMap.Tile<Character>> {

    @Override
    public Entity create(TileMap.Tile<Character> tile) {
        return new Entity()
                .bounds(tile.bounds())
                .add(new CameraTargetComponent());
    }
}
