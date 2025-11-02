package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.HasOrder;

import static dev.screwbox.core.environment.Order.PRESENTATION_NOTIFICATIONS;

@HasOrder(PRESENTATION_NOTIFICATIONS)
public class RenderNotificationsSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderNotifications();
    }
}
