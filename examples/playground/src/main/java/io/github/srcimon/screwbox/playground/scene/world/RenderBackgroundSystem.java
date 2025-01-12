package io.github.srcimon.screwbox.playground.scene.world;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Color;

@Order(Order.SystemOrder.PRESENTATION_BACKGROUND)
public class RenderBackgroundSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().canvas().fillWith(Color.hex("#001219"));
    }
}
