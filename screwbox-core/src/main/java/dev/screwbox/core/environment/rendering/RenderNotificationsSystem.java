package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

import static dev.screwbox.core.environment.Order.SystemOrder.PRESENTATION_NOTIFICATIONS;

@Order(PRESENTATION_NOTIFICATIONS)
public class RenderNotificationsSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderNotifications();
    }
}
