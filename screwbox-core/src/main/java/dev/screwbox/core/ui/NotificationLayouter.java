package dev.screwbox.core.ui;

import dev.screwbox.core.graphics.ScreenBounds;

/**
 * Calculates the {@link ScreenBounds} of a notification based on its value and the surrounding {@link ScreenBounds canvas bounds}.
 *
 * @since 2.8.0
 */
@FunctionalInterface
public interface NotificationLayouter {

    /**
     * Calculate the {@link ScreenBounds} of a notification.
     *
     * @param index        value of notification within visible notifications starting with 0
     * @param notification the notification that is rendered
     * @param canvasBounds the surrounding {@link ScreenBounds canvas bounds}
     */
    ScreenBounds layout(int index, Notification notification, ScreenBounds canvasBounds);
}
