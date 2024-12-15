package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.Objects;

public record Notification(String text) {

    public Notification {
        Objects.requireNonNull(text, "text must not be null");
        Validate.notEmpty(text, "text must not be empty");
    }

    public static Notification text(final String text) {
        return new Notification(text);
    }
}
