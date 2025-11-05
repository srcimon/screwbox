package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import static dev.screwbox.core.environment.Order.PRESENTATION_NOTIFICATIONS;

@ExecutionOrder(PRESENTATION_NOTIFICATIONS)
public class RenderNotificationsSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderNotifications();
    }
}
