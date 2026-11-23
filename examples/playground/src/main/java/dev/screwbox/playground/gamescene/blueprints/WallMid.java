package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.light.OrthographicWallComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.playground.gamescene.map.ShotObstacleComponent;
import dev.screwbox.tiled.Tile;

public class WallMid implements Blueprint<Tile> {

    @Override
    public Entity assembleFrom(Tile tile) {
        return new Entity()
            .bounds(tile.bounds())
            .add(new ColliderComponent())
            .add(new ShotObstacleComponent())
            .add(new ObstacleComponent())
            .add(new StaticBoundsComponent())
            .add(new OrthographicWallComponent())
            .add(new RenderComponent(tile.sprite(), tile.layer().order()));
    }
}
