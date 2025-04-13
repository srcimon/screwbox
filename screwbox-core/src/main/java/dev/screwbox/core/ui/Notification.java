package dev.screwbox.core.ui;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Sprite;

import java.util.Optional;

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

    /**
     * An optional icon to show on rendering of notification.
     */
    Optional<Sprite> icon();
}