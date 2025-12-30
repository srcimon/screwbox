package dev.screwbox.vacuum.enemies;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.vacuum.enemies.slime.Slime;

import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.core.environment.importing.ImportOptions.source;

public class EnemySpawnSystem implements EntitySystem {

    private static final Archetype SPAWN_POINTS = Archetype.of(SpawnPointComponent.class);
    private static final Archetype ENEMIES = Archetype.of(EnemyComponent.class);

    @Override
    public void update(Engine engine) {
        var enemyCount = engine.environment().entityCount(ENEMIES);
        for (final var spawnPoint : engine.environment().fetchAll(SPAWN_POINTS)) {
            final var spawnConfig = spawnPoint.get(SpawnPointComponent.class);
            if (enemyCount < 2 && spawnConfig.scheduler.isTick()) {
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
                        .lifespanSeconds(1));
                engine.environment().importSource(source(spawnPoint).make(new Slime()));
            }
        }
    }
}
