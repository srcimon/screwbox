package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.ImportContext;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.utils.TileMap;

public class Earth implements Blueprint<TileMap.Tile<Character>> {

    @Override
    public Entity create(TileMap.Tile<Character> tile, final ImportContext context) {
        return new Entity().bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                .add(new ColliderComponent())
                .add(new StaticColliderComponent());
    }
}
