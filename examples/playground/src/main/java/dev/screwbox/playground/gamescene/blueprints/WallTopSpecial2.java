package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.playground.gamescene.map.ShotObstacleComponent;
import dev.screwbox.tiled.Tile;

public class WallTopSpecial2 implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        return new Entity()
            .bounds(Bounds.atOrigin(tile.bounds().origin().addY(4), 16, 12))
            .add(new ColliderComponent())
            .add(new ShotObstacleComponent())
            .add(new ObstacleComponent())
            .add(new StaticBoundsComponent())
            .add(new OccluderComponent());
    }
}
