package dev.screwbox.vacuum.decoration;

import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.OrthographicWallComponent;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.light.StaticOccluderComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.navigation.ObstacleComponent;
import dev.screwbox.tiled.GameObject;

public class OrthographicWall implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(final GameObject object) {
        return new Entity().name("wall")
                .add(new OccluderComponent(false))
                .add(new StaticOccluderComponent())
                .add(new OrthographicWallComponent())
                .add(new ObstacleComponent())
                .add(new ColliderComponent())
                .add(new TransformComponent(object.bounds()));
    }
}
