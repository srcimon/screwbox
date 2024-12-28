package io.github.srcimon.screwbox.vacuum.decoration;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.OrthographicWallComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.light.StaticShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridObstacleComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class OrthographicWall implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity().name("wall")
                .add(new ShadowCasterComponent(false))
                .add(new StaticShadowCasterComponent())
                .add(new OrthographicWallComponent())
                .add(new PhysicsGridObstacleComponent())
                .add(new ColliderComponent())
                .add(new TransformComponent(object.bounds()));
    }
}
