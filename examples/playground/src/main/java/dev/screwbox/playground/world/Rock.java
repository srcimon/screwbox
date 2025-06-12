package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.core.graphics.AutoTileBundle;

public class Rock implements SourceImport.Converter<AsciiMap.Tile> {


    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity()
                .name("" + tile.autoTileMask())
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.findSprite(AutoTileBundle.ROCKS)))
                .add(new ColliderComponent(100));
    }
}
