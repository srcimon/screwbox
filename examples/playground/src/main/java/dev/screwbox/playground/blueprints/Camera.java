package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.ImportContext;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.utils.TileMap;

public class Camera implements Blueprint<TileMap.Tile<Character>> {

    @Override
    public Entity create(TileMap.Tile<Character> tile, final ImportContext context) {
        return new Entity()
                .bounds(tile.bounds())
                .add(new CameraTargetComponent());
    }
}
