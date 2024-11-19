package io.github.srcimon.screwbox.vacuum.player.attack;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.vacuum.enemies.EnemyComponent;
import io.github.srcimon.screwbox.vacuum.enemies.HurtComponent;

public class ShotUnderwayState implements EntityState {

    private static final Archetype ENEMIES = Archetype.ofSpacial(EnemyComponent.class);

    @Override
    public EntityState update(Entity entity, Engine engine) {
        for (final var enemy : engine.environment().fetchAll(ENEMIES)) {
            if (entity.bounds().touches(enemy.bounds())) {
                enemy.addIfNotPresent(new HurtComponent());
                return new ShotDissolvingState();
            }
        }

        return entity.get(CollisionDetectionComponent.class).collidedEntities.isEmpty()
                ? this
                : new ShotDissolvingState();
    }
}
