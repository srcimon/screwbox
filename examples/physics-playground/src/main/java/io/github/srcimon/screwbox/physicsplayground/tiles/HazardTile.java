package io.github.srcimon.screwbox.physicsplayground.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class HazardTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity("hazard-tile")
                .add(new TransformComponent(tile.renderBounds()))
                .add(new RenderComponent(tile.sprite()))
                .add(new ColliderComponent())
                .add(new StaticColliderComponent());
    }
}