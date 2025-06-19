package dev.screwbox.playground.world;

import dev.screwbox.core.creation.Tile;
import dev.screwbox.core.creation.TileMap;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.creation.AsciiMap;
import dev.screwbox.core.graphics.AutoTileBundle;

public class Rock implements SourceImport.Converter<Tile<Character>> {


    @Override
    public Entity convert(Tile<Character> tile) {
        return new Entity()
                .name(tile.autoTileMask().toString())
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                .add(new ColliderComponent(100));
    }
}
