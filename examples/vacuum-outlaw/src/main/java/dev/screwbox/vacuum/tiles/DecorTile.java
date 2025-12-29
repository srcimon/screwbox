package dev.screwbox.vacuum.tiles;

import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

public class DecorTile implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(final Tile tile) {
        return new Entity().name("DecorTile")
                .bounds(tile.bounds())
                .add(new RenderComponent(tile.sprite(), tile.layer().order()), render -> {
                    render.parallaxX = tile.layer().parallaxX();
                    render.parallaxY = tile.layer().parallaxY();
                });
    }
}
