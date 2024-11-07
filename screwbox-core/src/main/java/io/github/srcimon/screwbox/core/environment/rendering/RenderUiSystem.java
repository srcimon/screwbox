package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

@Order(Order.SystemOrder.PRESENTATION_UI_MENU)
public class RenderUiSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderMenu();
    }
}
