package io.github.simonbas.screwbox.examples.platformer.tiles;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.tiled.Tile;

public class SolidGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new TransformComponent(tile.renderBounds()),
                new StaticMarkerComponent(),
                new StaticShadowCasterMarkerComponent(),
                new ShadowCasterComponent(),
                new ColliderComponent(500, Percent.min()));
    }

}
