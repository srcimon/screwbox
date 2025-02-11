package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_NOTIFICATIONS;

@Order(PRESENTATION_NOTIFICATIONS)
public class RenderNotificationsSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        engine.ui().renderNotifications();
    }
}
