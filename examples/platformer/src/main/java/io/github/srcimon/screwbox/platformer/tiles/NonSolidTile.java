package io.github.srcimon.screwbox.platformer.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.tiled.Tile;

public class NonSolidTile implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity()
                .add(
                        new RenderComponent(tile.sprite().addBorder(2, Color.TRANSPARENT), tile.layer().order()),
                        renderComponent -> {
                            renderComponent.parallaxX = tile.layer().parallaxX();
                            renderComponent.parallaxY = tile.layer().parallaxY();
                        })
                .bounds(tile.renderBounds());
    }

}
