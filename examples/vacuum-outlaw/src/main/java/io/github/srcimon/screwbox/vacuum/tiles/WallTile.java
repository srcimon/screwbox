package io.github.srcimon.screwbox.vacuum.tiles;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.ShadowCasterComponent;
import dev.screwbox.core.environment.light.StaticShadowCasterComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiles.Tile;

public class WallTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity().name("wall")
                .add(new ColliderComponent())
                .add(new ShadowCasterComponent())
                .add(new StaticColliderComponent())
                .add(new PhysicsGridObstacleComponent())
                .add(new StaticShadowCasterComponent())
                .add(new RenderComponent(tile.sprite(), tile.layer().order()), r -> r.renderInForeground = true)
                .add(new TransformComponent(tile.renderBounds()));
    }
}
