package dev.screwbox.vacuum.player.attack;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.vacuum.enemies.EnemyComponent;
import dev.screwbox.vacuum.enemies.HurtComponent;

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

        return entity.get(CollisionSensorComponent.class).collidedEntities.isEmpty()
                ? this
                : new ShotDissolvingState();
    }
}
