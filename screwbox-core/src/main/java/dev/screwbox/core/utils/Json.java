package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO move inside tiled?
public class Json {


    private record Position(int start, int end) {

    }

    private record Attribute(Position namePosition, Position valuePosition, String name, String value) {

    }

    private record JsonObject(String content) {

        private JsonObject(final String content) {
            var trimmedContent = content.trim();
            Validate.isTrue(() -> trimmedContent.startsWith("{"), "input is no json string");
            Validate.isTrue(() -> trimmedContent.endsWith("}"), "input is no json string");
            this.content = trimmedContent.substring(1, trimmedContent.length() - 1).trim();

        }

        private List<Attribute> getAllAttributes() {
            List<Attribute> attributes = new ArrayList<>();
            int index = 0;
            while (index < content.length()) {
                var definition = fetchNextAttribute(index);
                if (!attributes.isEmpty()) {
                    var commaPosition = content.indexOf(',', attributes.getLast().valuePosition().end());
                    Validate.isTrue(() -> commaPosition < definition.namePosition().start() && commaPosition != -1, "malformatted json string: missing ',' between fields '%s' and '%s'".formatted(attributes.getLast().name, definition.name));
                }
                attributes.add(definition);
                index = definition.valuePosition().end() + 1;

            }
            return attributes;
        }


        private Attribute fetchNextAttribute(int index) {
            Position attributePosition = findName(index);

            var name = content.substring(attributePosition.start(), attributePosition.end());
            var dotsPosition = content.indexOf(':', attributePosition.end());
            Position valuePosition = findValue(attributePosition.end() + 1);
            Validate.isTrue(() -> dotsPosition < valuePosition.start() && dotsPosition != -1, "malformatted json string: missing ':' field '%s' and value".formatted(name));
            Validate.isNotEqual(valuePosition.end(), -1, "malformatted json string: missing '\"'");
            var value = content.substring(valuePosition.start(), valuePosition.end());
            return new Attribute(attributePosition, valuePosition, name, value);
        }

        private Position findName(final int index) {
            final var start = content.indexOf('\"', index) + 1;
            final var end = content.indexOf('\"', start);

            return new Position(start, end);
        }

        private Position findValue(final int index) {
            for (int i = index; i < content.length(); i++) {
                if (content.charAt(i) == '"') {
                    return findStringValue(index);//TODO we already know the start position -> optimize
                }
                if (Character.isDigit(content.charAt(i))) {
                    return findIntegerValue(i);
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing value for attribute");
        }

        //TODO support .0 values
        //TODO support double values
        //TODO support float values
        private Position findIntegerValue(final int index) {
            for (int i = index; i < content.length(); i++) {
                if (!Character.isDigit(content.charAt(i))) {
                    return new Position(index, i);
                }
            }
            return new Position(index, content.length());
        }

        private Position findStringValue(final int index) {
            final var start = content.indexOf('\"', index) + 1;
            final var end = content.indexOf('\"', start + 1);
            Validate.isNotEqual(end, -1, "malformatted json string: missing '\"");
            return new Position(start, end);
        }

        public <T> T getValue(final Field field) {
            return (T) getAllAttributes().stream()
                .filter(attribute -> attribute.name().equals(field.getName()))
                .findFirst()
                .map(attribute -> toInstance(attribute.value(), field.getType()))
                .orElse(defaultForType(field.getType()));
        }

        private <T> T toInstance(String value, Class<T> type) {
            if (type.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) type, value);
            }
            return switch (type.getSimpleName()) {
                case "String" -> (T) value;
                case "Integer", "int" -> (T) Integer.valueOf(value);
                default -> throw new IllegalArgumentException("unsupported type: " + type);
            };
        }

        private static <T> T defaultForType(final Class<?> type) {
            return switch (type.getSimpleName()) {
                case "Integer", "int" -> (T) Integer.valueOf(0);
                case "Boolean", "boolean" -> (T) Boolean.FALSE;
                default -> null;
            };
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
                values[i] = content.getValue(field);
            }
            return (T) constructor.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("could not instantiate type " + type.getSimpleName(), e);
        }
    }
}
