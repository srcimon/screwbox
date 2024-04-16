package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import static io.github.srcimon.screwbox.core.Vector.$;

/**
 * Enables chaotic movement behaviour for all {@link Entity}s having {@link PhysicsComponent} and {@link ChaoticMovementComponent}.
 */
@Order(SystemOrder.PREPARATION)
public class ChaoticMovementSystem implements EntitySystem {

    private static final Archetype MOVING_ENTITIES = Archetype.of(PhysicsComponent.class, ChaoticMovementComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(MOVING_ENTITIES)) {
            final var movement = entity.get(ChaoticMovementComponent.class);
            final Vector chaoticMovement = $(
                    movement.xModifier.value(engine.loop().lastUpdate()),
                    movement.yModifier.value(engine.loop().lastUpdate()))
                    .multiply(movement.speed).multiply(engine.loop().delta());
            entity.moveTo(entity.position().add(chaoticMovement));
        }
    }
}
