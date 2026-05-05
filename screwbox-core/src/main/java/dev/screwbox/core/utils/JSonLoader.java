package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JSonLoader {

    public static <T> T load(final String json, final Class<T> type) {
        Objects.requireNonNull(json, "json must not be null");
        Objects.requireNonNull(type, "type must not be null");

        final var allArgsConstructor = Reflections.findAllArgsConstructor(type)
            .orElseThrow(() -> new IllegalArgumentException(type.getSimpleName() + " is missing all args constructor"));

        final Map<String, String> fieldData = getFieldData(json);


        return createInstance(allArgsConstructor);
    }

    private static HashMap<String, String> getFieldData(final String json) {
        Validate.isTrue(() -> json.startsWith("{"), "input is no json string");
        Validate.isTrue(() -> json.endsWith("}"), "input is no json string");
        var jsonContent = json.substring(1, json.length() - 1);
        return new HashMap<>();
    }

    private static <T> T createInstance(final Constructor<T> allArgsConstructor) {
        try {
            return (T) allArgsConstructor.newInstance((String) null);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("could not instantiate type " + allArgsConstructor.getDeclaringClass().getSimpleName(), e);
        }
    }
}
