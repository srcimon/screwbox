package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.vacuum.enemies.slime.Slime;

public class EnemySpawnSystem implements EntitySystem {

    private static final Archetype SPAWN_POINTS = Archetype.of(SpawnPointComponent.class);
    private static final Archetype ENEMIES = Archetype.of(EnemyComponent.class);

    @Override
    public void update(Engine engine) {
        var enemyCount = engine.environment().fetchAll(ENEMIES).size();
        for (final var spawnPoint : engine.environment().fetchAll(SPAWN_POINTS)) {
            final var spawnConfig = spawnPoint.get(SpawnPointComponent.class);
            if (spawnConfig.sheduler.isTick()) {
                if (enemyCount < 2) {
                    enemyCount++;
                    engine.environment().importSource(spawnPoint).as(new Slime());
                }
            }
        }
    }
}
