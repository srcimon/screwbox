package io.github.srcimon.screwbox.physicsplayground.tiles;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.StaticColliderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.tiled.Tile;

public class SolidTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(Tile tile) {
        return new Entity("solid-tile")
                .add(new TransformComponent(tile.renderBounds()))
                .add(new RenderComponent(tile.sprite()))
                .add(new FloorTypeComponent(tile.properties().tryGetString("type").orElse("solid").equals("solid")
                        ? FloorType.SOLID
                        : FloorType.SOFT))
                .add(new ColliderComponent())
                .add(new StaticColliderComponent());
    }
}
