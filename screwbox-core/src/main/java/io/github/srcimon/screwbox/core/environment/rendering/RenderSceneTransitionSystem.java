package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

@Order(Order.SystemOrder.SCENE_TRANSITIONS)
public class RenderSceneTransitionSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.scenes().renderSceneTransition();
    }
}
