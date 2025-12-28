package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.AdvancedBlueprint;
import dev.screwbox.core.environment.ImportContext;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.utils.TileMap;

public class Camera implements AdvancedBlueprint<TileMap.Tile<Character>> {

    @Override
    public Entity assembleFrom(TileMap.Tile<Character> tile, final ImportContext context) {
        return new Entity()
                .bounds(tile.bounds())
                .add(new CameraTargetComponent());
    }
}
