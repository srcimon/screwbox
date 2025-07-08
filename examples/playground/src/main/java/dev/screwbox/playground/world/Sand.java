package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.AutoTileBundle;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

public class Sand implements SourceImport.Converter<TileMap.Tile<Color>> {


    @Override
    public Entity convert(TileMap.Tile<Color> tile) {
        return new Entity()
                .name(tile.autoTileMask().toString())
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.CANDYLAND)))
                .add(new ColliderComponent(100));
    }
}
