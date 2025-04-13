package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

@Order(Order.SystemOrder.PRESENTATION_UI_MENU)
public class RenderUiSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderMenu();
    }
}
