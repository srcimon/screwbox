package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Screen;

/**
 * A notification visible on {@link Screen}.
 *
 * @since 2.8.0
 */
public interface Notification {

    /**
     * The {@link Time} the notification was created.
     */
    Time timeCreated();

    /**
     * The text content of the notification.
     */
    String text();

    /**
     * The progress of the notification before getting removed.
     */
    Percent progress();
}