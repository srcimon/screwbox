package dev.screwbox.playground.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.population.EntityBlueprint;
import dev.screwbox.core.environment.population.ImportContext;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.utils.TileMap;

public class Block implements EntityBlueprint<TileMap.Tile> {

    @Override
    public Entity createFrom(final TileMap.Tile tile, final ImportContext context) {
        return new Entity()
                .name("block")
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                .add(new ColliderComponent())
                .add(new StaticColliderComponent());
    }

}
