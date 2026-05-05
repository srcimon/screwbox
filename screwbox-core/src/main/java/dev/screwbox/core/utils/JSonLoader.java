package dev.screwbox.core.utils;

import java.util.Objects;

public class JSonLoader {

    public static <T> T load(final String json, Class<T> type) {
        Objects.requireNonNull(json, "json must not be null");
        Objects.requireNonNull(type, "type must not be null");
        return null;
    }
}
