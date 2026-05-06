package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Json {

    private record Attribute(int endIndex, String name, String value) {

    }

    private record JsonObject(String content) {

        private JsonObject(final String content) {
            Validate.isTrue(() -> content.startsWith("{"), "input is no json string");
            Validate.isTrue(() -> content.endsWith("}"), "input is no json string");
            this.content = content.substring(1, content.length() - 1).trim();

        }

        private List<Attribute> getAllAttributes() {
            List<Attribute> attributes = new ArrayList<>();
            int index = 0;
            while (index < content.length()) {
                var next = fetchAttribute(index);
                attributes.add(next);
                index = next.endIndex()+1;
            }
            return attributes;
        }

        private Attribute fetchAttribute(int index) {
            var attributeStart = content.indexOf('\"', index);
            var attributeEnd = content.indexOf('\"', attributeStart + 1);
            var name = content.substring(attributeStart + 1, attributeEnd);

            var attributeValueStart = content.indexOf('\"', attributeEnd+1);
            var attributeValueEnd = content.indexOf('\"', attributeValueStart + 1);
            var value = content.substring(attributeValueStart + 1, attributeValueEnd);
            return new Attribute(attributeValueEnd+1, name, value);
        }

        public <T> T getValue(final String name, final Class<T> type) {
            return (T)getAllAttributes().stream().filter(attribute -> attribute.name().equals(name)).findFirst().map(Attribute::value).orElse(null);
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
