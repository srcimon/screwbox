package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.OPTIMIZATION;

@ExecutionOrder(OPTIMIZATION)
public class OptimizePhysicsPerformanceSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.environment().bake(StaticColliderComponent.class, ColliderComponent.class);
    }
}
