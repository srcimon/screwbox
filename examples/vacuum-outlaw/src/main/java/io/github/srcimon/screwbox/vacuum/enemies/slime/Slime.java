package io.github.srcimon.screwbox.vacuum.enemies.slime;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.vacuum.enemies.EnemyComponent;
import io.github.srcimon.screwbox.vacuum.enemies.RunAtPlayerComponent;
import io.github.srcimon.screwbox.vacuum.enemies.SpawnPointComponent;

public class Slime implements SourceImport.Converter<Entity> {

    @Override
    public Entity convert(Entity spawnPoint) {
        return new Entity().name("slime")
                .add(new TransformComponent(spawnPoint.position(), 8, 8))
                .add(new RenderComponent(SpriteBundle.SLIME_MOVING, spawnPoint.get(SpawnPointComponent.class).drawOrder))
                .add(new PhysicsComponent())
                .add(new ColliderComponent())
                .add(new EnemyComponent())
                .add(new RunAtPlayerComponent())
                .add(new ShadowCasterComponent(false))
                .add(new AutomovementComponent(40));
    }
}
