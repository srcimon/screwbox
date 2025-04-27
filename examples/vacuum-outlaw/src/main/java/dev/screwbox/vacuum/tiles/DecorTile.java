package dev.screwbox.vacuum.tiles;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

public class DecorTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity().name("DecorTile")
                .bounds(tile.renderBounds())
                .add(new RenderComponent(tile.sprite(), tile.layer().order()), render -> {
                    render.parallaxX = tile.layer().parallaxX();
                    render.parallaxY = tile.layer().parallaxY();
                });
    }
}
