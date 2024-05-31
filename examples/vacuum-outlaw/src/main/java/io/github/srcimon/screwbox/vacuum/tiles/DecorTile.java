package io.github.srcimon.screwbox.vacuum.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class DecorTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity().name("DecorTile")
                .add(new TransformComponent(tile.renderBounds()))
                .addCustomized(new RenderComponent(tile.sprite(), tile.layer().order()), render -> {
                    render.parallaxX = tile.layer().parallaxX();
                    render.parallaxY = tile.layer().parallaxY();
                });
    }
}
