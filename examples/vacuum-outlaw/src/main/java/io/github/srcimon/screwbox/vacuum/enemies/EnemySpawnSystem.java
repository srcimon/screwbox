package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.vacuum.enemies.slime.Slime;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class EnemySpawnSystem implements EntitySystem {

    private static final Archetype SPAWN_POINTS = Archetype.of(SpawnPointComponent.class);
    private static final Archetype ENEMIES = Archetype.of(EnemyComponent.class);

    @Override
    public void update(Engine engine) {
        var enemyCount = engine.environment().fetchAll(ENEMIES).size();
        for (final var spawnPoint : engine.environment().fetchAll(SPAWN_POINTS)) {
            final var spawnConfig = spawnPoint.get(SpawnPointComponent.class);
            if (spawnConfig.sheduler.isTick() && enemyCount < 2) {
                enemyCount++;
                engine.particles().spawnMultiple(4, spawnPoint.position(), ParticleOptions.unknownSource()
                        .sprite(SpriteBundle.ELECTRICITY_SPARCLE)
                        .ease(Ease.SINE_IN_OUT)
                        .randomStartScale(1, 2)
                        .startOpacity(Percent.zero())
                        .animateOpacity(Percent.zero(), Percent.of(0.6))
                        .chaoticMovement(50, ofSeconds(1))
                        .drawOrder(spawnConfig.drawOrder)
                        .randomStartRotation()
                        .lifetimeSeconds(1));
                engine.environment().importSource(spawnPoint).as(new Slime());
            }
        }
    }
}
