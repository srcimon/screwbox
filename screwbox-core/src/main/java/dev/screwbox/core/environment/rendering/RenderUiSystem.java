package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;

@ExecutionOrder(Order.PRESENTATION_UI_MENU)
public class RenderUiSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderMenu();
    }
}
