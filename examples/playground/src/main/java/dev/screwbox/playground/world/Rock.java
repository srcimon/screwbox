package dev.screwbox.playground.world;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.AsciiMap;
import dev.screwbox.core.utils.AutoTile;

public class Rock implements SourceImport.Converter<AsciiMap.Tile> {

    AutoTile autoTile = AutoTile.fromDummyFile("assets/autotiles/template.png");//TODO AutoTileBundle.ROCKS

    @Override
    public Entity convert(AsciiMap.Tile tile) {
        return new Entity()
                .name("" + tile.bitmask().index())
                .bounds(tile.bounds())
                .add(new RenderComponent(autoTile.spriteForIndex(tile.bitmask().index())))
                .add(new ColliderComponent(100));
    }
}
