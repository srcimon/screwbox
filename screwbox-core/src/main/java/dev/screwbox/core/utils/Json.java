package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Json {

    private record JsonObject(String content) {

        private JsonObject(final String content) {
            Validate.isTrue(() -> content.startsWith("{"), "input is no json string");
            Validate.isTrue(() -> content.endsWith("}"), "input is no json string");
            this.content = content.substring(1, content.length() - 1).trim();
        }

        public <T> T getValue(final String name, final Class<T> type) {
            return (T) findAttributeValue(name);
        }

        private String findAttributeValue(String name) {
            return null;
        }
    }

    public static <T> T load(final String json, final Class<T> type) {
        Objects.requireNonNull(json, "json must not be null");
        Objects.requireNonNull(type, "type must not be null");

        final var allArgsConstructor = Reflections.findAllArgsConstructor(type)
            .orElseThrow(() -> new IllegalArgumentException(type.getSimpleName() + " is missing all args constructor"));

        final var object = new JsonObject(json);
        return createInstance(allArgsConstructor, object);
    }

    private static <T> T createInstance(final Constructor<T> constructor, final JsonObject content) {
        final Class<T> type = constructor.getDeclaringClass();
        try {
            final Object[] values = new Object[type.getDeclaredFields().length];
            for (int i = 0; i < values.length; i++) {
                final var field = type.getDeclaredFields()[i];
                values[i] = content.getValue(field.getName(), field.getType());
            }
            return (T) constructor.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("could not instantiate type " + type.getSimpleName(), e);
        }
    }
}
