package io.github.simonbas.screwbox.examples.platformer.tiles;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.StaticMarkerComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.tiled.Tile;

public class OneWayGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new StaticMarkerComponent(),
                new TransformComponent(tile.renderBounds()),
                new ColliderComponent(500, Percent.min(), true));
    }

}
