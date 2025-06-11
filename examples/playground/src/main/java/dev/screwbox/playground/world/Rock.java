package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.core.utils.AutoTile;

public class Rock implements SourceImport.Converter<AsciiMap.Tile> {

    AutoTile autoTile = AutoTile.fromSpriteSheet("assets/autotiles/rocks.png");//TODO AutoTileBundle.ROCKS, TEMPLATE

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity()
                .name("" + tile.autoTileIndex().index3x3Minimal())
                .bounds(tile.bounds())
                .add(new RenderComponent(autoTile.spriteForIndex(tile.autoTileIndex())))
                .add(new ColliderComponent(100));
    }
}
