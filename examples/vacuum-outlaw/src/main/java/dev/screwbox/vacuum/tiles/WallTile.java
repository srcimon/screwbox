package dev.screwbox.vacuum.tiles;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

import static dev.screwbox.core.environment.Order.SystemOrder.PRESENTATION_UI;

public class WallTile implements SourceImport.Converter<Tile> {

    @Override
    public Entity convert(final Tile tile) {
        return new Entity().name("wall")
                .add(new ColliderComponent())
                .add(new OccluderComponent())
                .add(new StaticColliderComponent())
                .add(new ObstacleComponent())
                .add(new StaticOccluderComponent())
                .add(new RenderComponent(tile.sprite(), Order.SystemOrder.PRESENTATION_LIGHT.orderPlus(1)))
                .add(new TransformComponent(tile.bounds()));
    }
}
