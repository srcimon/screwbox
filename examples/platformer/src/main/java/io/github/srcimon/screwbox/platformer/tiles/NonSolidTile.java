package io.github.srcimon.screwbox.platformer.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

import static io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions.originalSize;

public class NonSolidTile implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity()
                .addCustomized(
                        new RenderComponent(tile.sprite(), tile.layer().order(), originalSize().opacity(tile.layer().opacity())),
                        renderComponent -> {
                            renderComponent.parallaxX = tile.layer().parallaxX();
                            renderComponent.parallaxY = tile.layer().parallaxY();
                        })
                .bounds(tile.renderBounds());
    }

}
