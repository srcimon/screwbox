package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;

/**
 * Calculates the {@link ScreenBounds} of a notification based on its index and the surrounding {@link ScreenBounds canvas bounds}.
 */
@FunctionalInterface
public interface NotificationLayouter {

    /**
     * Calculate the {@link ScreenBounds} of a notification.
     *
     * @param index        index of notification within visible notifications starting with 0
     * @param notification the notification that is renderd
     * @param canvasBounds the surrounding {@link ScreenBounds canvas bounds}
     */
    ScreenBounds layout(int index, Notification notification, ScreenBounds canvasBounds);
}
