package io.github.srcimon.screwbox.vacuum.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.light.StaticShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class DecorTop implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity().name("wall")
                .add(new ShadowCasterComponent())
                .add(new PhysicsGridObstacleComponent())
                .add(new StaticShadowCasterComponent())
                .addCustomized(new RenderComponent(tile.sprite(), tile.layer().order()), r -> r.renderOverLight = true)
                .add(new TransformComponent(tile.renderBounds()));
    }
}
