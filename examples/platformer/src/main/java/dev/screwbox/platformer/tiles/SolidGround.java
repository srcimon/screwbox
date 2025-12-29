package dev.screwbox.platformer.tiles;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.StaticColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.tiled.Tile;

public class SolidGround implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        return new Entity().add(
                new RenderComponent(tile.sprite(), tile.layer().order()),
                new TransformComponent(tile.bounds()),
                new StaticColliderComponent(),
                new StaticOccluderComponent(),
                new OccluderComponent(),
                new ColliderComponent(500, Percent.zero()));
    }

}
