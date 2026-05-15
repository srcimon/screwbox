package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.keyboard.Key;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class IlluminationDebugSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        for (var entity : engine.environment().fetchAllHaving(IlluminationComponent.class)) {
            if (!engine.keyboard().isDown(Key.SPACE)) {
                engine.graphics().light().addIllumination(entity.position(), entity.get(IlluminationComponent.class).radius, Color.BLACK);
            }
        }
    }
}
