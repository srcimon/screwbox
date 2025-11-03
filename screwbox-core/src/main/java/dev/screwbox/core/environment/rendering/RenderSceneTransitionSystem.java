package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;

@ExecutionOrder(Order.PRESENTATION_TRANSITIONS)
public class RenderSceneTransitionSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.scenes().renderTransition();
    }
}
