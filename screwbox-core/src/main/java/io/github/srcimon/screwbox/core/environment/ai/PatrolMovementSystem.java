package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.core.physics.Physics;
import io.github.srcimon.screwbox.core.utils.Scheduler;

/**
 * Applies a patrolling movement pattern to {@link Entity entities} having a {@link PhysicsComponent} and a {@link PatrolMovementComponent}.
 *
 * @since 2.12.0
 */
public class PatrolMovementSystem implements EntitySystem {

    private static final Archetype PATROLS = Archetype.of(PhysicsComponent.class, PatrolMovementComponent.class);
    private final Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(50));

    @Override
    public void update(final Engine engine) {
        final boolean checkForRouteChange = scheduler.isTick(engine.loop().time());
        for (final var entity : engine.environment().fetchAll(PATROLS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var patrollingMovement = entity.get(PatrolMovementComponent.class);
            final boolean isGoingRight = physics.momentum.x() > 0;

            final boolean mustChangeDirection = checkForRouteChange
                    && checkForRouteChangeIsTriggerd(engine.physics(), entity, isGoingRight);

            final boolean faceRight = isGoingRight != mustChangeDirection;
            double newX = faceRight ? patrollingMovement.speed : -patrollingMovement.speed;
            physics.momentum = physics.momentum.replaceX(newX);
        }
    }

    private boolean checkForRouteChangeIsTriggerd(final Physics physics, final Entity entity, final boolean isGoingRight) {
        final var raycast = physics
                .raycastFrom(isGoingRight ? entity.bounds().bottomRight().addY(-0.1) : entity.bounds().bottomLeft().addY(-0.1))
                .ignoringEntitiesNotIn(entity.bounds().expand(0.2))
                .ignoringEntities(entity);
        return raycast.checkingBorders(Borders.TOP_ONLY).castingVertical(0.2).noHit()
                || raycast.checkingBorders(Borders.HORIZONTAL_ONLY).castingHorizontal(isGoingRight ? 0.2 : -0.2).hasHit();
    }
}
