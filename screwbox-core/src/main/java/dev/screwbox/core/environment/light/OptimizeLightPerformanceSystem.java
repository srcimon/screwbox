package dev.screwbox.core.environment.light;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.OPTIMIZATION;

@ExecutionOrder(OPTIMIZATION)
public class OptimizeLightPerformanceSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
     //TODO remove system entirely   engine.environment().bake(StaticOccluderComponent.class, OccluderComponent.class);
    }
}
