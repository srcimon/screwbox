package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.function.Supplier;

public record NotificationDetails(String text, Sprite icon) {

    public NotificationDetails {
        Objects.requireNonNull(text, "text must not be null");
        Validate.notEmpty(text, "text must not be empty");
    }

    public static NotificationDetails text(final String text) {
        return new NotificationDetails(text, null);
    }

    public NotificationDetails icon(final Supplier<Sprite> icon) {
        Objects.requireNonNull(text, "icon must not be null");
        return icon(icon.get());
    }

    public NotificationDetails icon(final Sprite icon) {
        return new NotificationDetails(text, icon);
    }
}
