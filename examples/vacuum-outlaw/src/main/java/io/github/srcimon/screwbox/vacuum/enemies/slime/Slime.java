package io.github.srcimon.screwbox.vacuum.enemies.slime;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.physics.MovementPathComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.vacuum.enemies.EnemyComponent;
import io.github.srcimon.screwbox.vacuum.enemies.RunAtPlayerComponent;
import io.github.srcimon.screwbox.vacuum.enemies.SpawnPointComponent;

public class Slime implements SourceImport.Converter<Entity> {

    @Override
    public Entity convert(Entity spawnPoint) {
        int drawOrder = spawnPoint.get(SpawnPointComponent.class).drawOrder;
        return new Entity().name("slime")
                .add(new TransformComponent(spawnPoint.position(), 8, 8))
                .add(new RenderComponent(SpriteBundle.SLIME_MOVING, drawOrder))
                .add(new PhysicsComponent())
                .add(new ColliderComponent())
                .add(new EnemyComponent())
                .add(new ParticleEmitterComponent(Duration.ofMillis(120), ParticleEmitterComponent.SpawnMode.POSITION, ParticleOptions.unknownSource()
                        .sprite(SpriteBundle.DOT_YELLOW)
                        .animateScale(0.0, 0.6)
                        .randomLifeTimeSeconds(2,4)
                        .ease(Ease.PLATEAU_OUT)
                        .animateOpacity()
                        .drawOrder(drawOrder - 1)))
                .add(new RunAtPlayerComponent())
                .add(new ShadowCasterComponent(false))
                .add(new MovementPathComponent(40, 160));
    }
}
