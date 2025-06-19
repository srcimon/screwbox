package dev.screwbox.playground.world;

import dev.screwbox.core.creation.TileMap;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;

public class Rock implements SourceImport.Converter<TileMap.Tile<Character>> {


    @Override
    public Entity convert(TileMap.Tile<Character> tile) {
        return new Entity()
                .name(tile.autoTileMask().toString())
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                .add(new ColliderComponent(100));
    }
}
