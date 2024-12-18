package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Specifies attributes of a {@link Notification}.
 *
 * @param text  text of the {@link Notification}
 * @param icon  icon of the {@link Notification}
 * @param sound souns to be played when showing the {@link Notification}
 * @since 2.8.0
 */
public record NotificationDetails(String text, Optional<Sprite> icon, Optional<Sound> sound) {

    public NotificationDetails {
        Objects.requireNonNull(text, "text must not be null");
        Validate.notEmpty(text, "text must not be empty");
    }

    /**
     * Creates a new instance with the specified text.
     */
    public static NotificationDetails text(final String text) {
        return new NotificationDetails(text, Optional.empty(), Optional.empty());
    }

    /**
     * Returns a new instance with the specified icon.
     */
    public NotificationDetails icon(final Supplier<Sprite> icon) {
        Objects.requireNonNull(text, "icon must not be null");
        return icon(icon.get());
    }

    /**
     * Returns a new instance with the specified sound.
     */
    public NotificationDetails sound(final Supplier<Sound> sound) {
        Objects.requireNonNull(sound, "sound must not be null");
        return sound(sound.get());
    }

    public NotificationDetails sound(final Sound sound) {
        return new NotificationDetails(text, icon, Optional.of(sound));
    }

    public NotificationDetails icon(final Sprite icon) {
        return new NotificationDetails(text, Optional.of(icon), sound);
    }

}

