package dev.screwbox.vacuum.tiles;

import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

import static dev.screwbox.core.environment.Order.PRESENTATION_LIGHT;

public class WallTile implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(final Tile tile) {
        return new Entity().name("wall")
                .add(new ColliderComponent())
                .add(new OccluderComponent())
                .add(new StaticColliderComponent())
                .add(new ObstacleComponent())
                .add(new StaticOccluderComponent())
                .add(new RenderComponent(tile.sprite(), PRESENTATION_LIGHT.mixinDrawOrder(1)))
                .add(new TransformComponent(tile.bounds()));
    }
}
