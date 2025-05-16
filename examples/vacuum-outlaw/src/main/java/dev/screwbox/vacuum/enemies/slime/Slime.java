package dev.screwbox.vacuum.enemies.slime;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.ShadowCasterComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.vacuum.enemies.EnemyComponent;
import dev.screwbox.vacuum.enemies.RunAtPlayerComponent;
import dev.screwbox.vacuum.enemies.SpawnPointComponent;

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
                .add(new ParticleEmitterComponent(Duration.ofMillis(120), SpawnMode.POSITION, ParticleOptions.unknownSource()
                        .sprite(SpriteBundle.DOT_YELLOW)
                        .randomStartScale(0.4, 0.8)
                        .randomLifeTimeSeconds(6,7)
                        .ease(Ease.PLATEAU_OUT_SLOW)
                        .animateOpacity()
                        .drawOrder(drawOrder - 1)))
                .add(new RunAtPlayerComponent())
                .add(new ShadowCasterComponent(false))
                .add(new PathMovementComponent(40, 160));
    }
}
