package io.github.srcimon.screwbox.examples.platformer.tiles;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.light.StaticShadowCasterComponent;
import io.github.srcimon.screwbox.tiled.Tile;
import io.github.srcimon.screwbox.core.environment.components.*;

public class SolidGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new TransformComponent(tile.renderBounds()),
                new StaticMarkerComponent(),
                new StaticShadowCasterComponent(),
                new ShadowCasterComponent(),
                new ColliderComponent(500, Percent.min()));
    }

}
