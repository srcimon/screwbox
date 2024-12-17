package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.function.Supplier;

//TODO javadoc

public record NotificationDetails(String text, Sprite icon, Sound sound) {

    //TODO test
    public NotificationDetails {
        Objects.requireNonNull(text, "text must not be null");
        Validate.notEmpty(text, "text must not be empty");
    }

    public static NotificationDetails text(final String text) {
        return new NotificationDetails(text, null, null);
    }

    public NotificationDetails icon(final Supplier<Sprite> icon) {
        Objects.requireNonNull(text, "icon must not be null");
        return icon(icon.get());
    }

    public NotificationDetails sound(final Supplier<Sound> sound) {
        Objects.requireNonNull(sound, "sound must not be null");
        return sound(sound.get());
    }

    public NotificationDetails sound(final Sound sound) {
        return new NotificationDetails(text, icon, sound);
    }

    public NotificationDetails icon(final Sprite icon) {
        return new NotificationDetails(text, icon, sound);
    }

}

