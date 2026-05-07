package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO move inside tiled?
public class Json {


    private record Position(int startIndex, int endIndex) {

    }

    private record Attribute(Position namePosition, Position valuePosition, String name, String value) {

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
                var definition = fetchNextAttribute(index);
                if (!attributes.isEmpty()) {
                    var commaPosition = content.indexOf(',', attributes.getLast().valuePosition().endIndex());
                    Validate.isTrue(() -> commaPosition < definition.namePosition().startIndex() && commaPosition != -1, "malformatted json string: missing ',' between fields '%s' and '%s'".formatted(attributes.getLast().name, definition.name));
                }
                attributes.add(definition);
                index = definition.valuePosition().endIndex() + 1;

            }
            return attributes;
        }


        private Attribute fetchNextAttribute(int index) {
            Position attributePosition = findName(index);

            var name = content.substring(attributePosition.startIndex(), attributePosition.endIndex());
            var dotsPosition = content.indexOf(':', attributePosition.endIndex());
            Position valuePosition = findValue(attributePosition.endIndex());
            Validate.isTrue(() -> dotsPosition < valuePosition.startIndex() && dotsPosition != -1, "malformatted json string: missing ':' field '%s' and value".formatted(name));
            Validate.isNotEqual(valuePosition.endIndex(), -1, "malformatted json string: missing '\"'");
            var value = content.substring(valuePosition.startIndex(), valuePosition.endIndex());
            return new Attribute(attributePosition, valuePosition, name, value);
        }

        private Position findName(final int index) {
            final var start = content.indexOf('\"', index) + 1;
            final var end = content.indexOf('\"', start);

            return new Position(start, end);
        }

        private Position findValue(final int index) {
            final var start = content.indexOf('\"', index + 1) + 1;
            final var end = content.indexOf('\"', start + 1);
            return new Position(start, end);
        }

        public <T> T getValue(final String name, final Class<T> type) {
            return (T) getAllAttributes().stream().filter(attribute -> attribute.name().equals(name)).findFirst().map(Attribute::value).orElse(null);
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
