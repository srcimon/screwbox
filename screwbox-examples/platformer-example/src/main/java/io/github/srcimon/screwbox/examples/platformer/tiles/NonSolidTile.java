package io.github.srcimon.screwbox.examples.platformer.tiles;

import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class NonSolidTile implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order(), tile.layer().opacity()),
                new TransformComponent(tile.renderBounds()));
    }

}
