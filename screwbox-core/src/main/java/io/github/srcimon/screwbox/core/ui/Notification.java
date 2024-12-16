package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;
import java.util.function.Supplier;

public record Notification(String text, Sprite icon) {

    public Notification {
        Objects.requireNonNull(text, "text must not be null");
        Validate.notEmpty(text, "text must not be empty");
    }

    public static Notification text(final String text) {
        return new Notification(text, null);
    }

    public Notification icon(final Supplier<Sprite> icon) {
        Objects.requireNonNull(text, "icon must not be null");
        return icon(icon.get());
    }

    public Notification icon(final Sprite icon) {
        return new Notification(text, icon);
    }
}
