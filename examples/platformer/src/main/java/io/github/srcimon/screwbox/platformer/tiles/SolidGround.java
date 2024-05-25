package io.github.srcimon.screwbox.platformer.tiles;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.LightBlockingComponent;
import io.github.srcimon.screwbox.core.environment.light.StaticLightBlockingComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class SolidGround implements Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new TransformComponent(tile.renderBounds()),
                new StaticColliderComponent(),
                new StaticLightBlockingComponent(),
                new LightBlockingComponent(),
                new ColliderComponent(500, Percent.zero()));
    }

}
