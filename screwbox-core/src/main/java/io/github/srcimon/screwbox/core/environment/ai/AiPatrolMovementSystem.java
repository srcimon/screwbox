package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.core.physics.RaycastBuilder;
import io.github.srcimon.screwbox.core.utils.Sheduler;

//TODO javadoc
//TODO changelog
//TODO addFeature
//TODO iteratingEntitySystem (?)
public class AiPatrolMovementSystem implements EntitySystem {

    private static final Archetype PATROLS = Archetype.of(PhysicsComponent.class, AiPatrolMovementComponent.class);
    public final Sheduler sheduler = Sheduler.withInterval(Duration.ofMillis(50));

    @Override
    public void update(final Engine engine) {
        if (sheduler.isTick()) {
            for (final var entity : engine.environment().fetchAll(PATROLS)) {
                final var physics = entity.get(PhysicsComponent.class);
                final var patrollingMovement = entity.get(AiPatrolMovementComponent.class);
                final boolean isGoingRight = physics.momentum.x() > 0;

                RaycastBuilder raycast = engine.physics()
                        .raycastFrom(isGoingRight ? entity.bounds().bottomRight() : entity.bounds().bottomLeft())
                        .ignoringEntitesNotIn(entity.bounds().expand(0.2))
                        .ignoringEntities(entity);
                final boolean mustChangeDirection = raycast.checkingBorders(Borders.TOP_ONLY).castingVertical(0.1).noHit()
                        || raycast.checkingBorders(Borders.HORIZONTAL_ONLY).castingHorizontal(isGoingRight ? 0.1 : -0.1).hasHit();
                final boolean faceRight = (isGoingRight && !mustChangeDirection) || (!isGoingRight && mustChangeDirection);//TODO simplify?
                physics.momentum = physics.momentum.replaceX(faceRight ? patrollingMovement.speed : -patrollingMovement.speed);

            }
        } else {
            for (final var entity : engine.environment().fetchAll(PATROLS)) {
                final var physics = entity.get(PhysicsComponent.class);
                final boolean isGoingRight = physics.momentum.x() > 0;
                final var patrollingMovement = entity.get(AiPatrolMovementComponent.class);
                physics.momentum = physics.momentum.replaceX(isGoingRight ? patrollingMovement.speed : -patrollingMovement.speed);
            }
        }
    }
}
