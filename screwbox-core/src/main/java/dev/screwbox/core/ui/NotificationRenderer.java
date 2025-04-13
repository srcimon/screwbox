package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.ScreenBounds;

/**
 * Renders a notification on the {@link Canvas} using {@link ScreenBounds} provided by {@link NotificationLayouter}.
 *
 * @since 2.8.0
 */
@FunctionalInterface
public interface NotificationRenderer {

    /**
     * Render the {@link Notification} on the {@link Canvas}.
     *
     * @param notification {@link Notification} to render
     * @param bounds       {@link ScreenBounds} calculated by {@link NotificationLayouter}
     * @param canvas       {@link Canvas} for drawing onto
     */
    void render(Notification notification, ScreenBounds bounds, Canvas canvas);
}
