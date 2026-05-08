package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//TODO move inside tiled?
public class Json {

    private static final Set<Character> WHITESPACE_CHARS = Set.of(' ', '\t', '\n', '\r');
    private static final Set<Character> FLOW_CHARACTERS = Set.of(':', '{', '}', ',');

    private record Position(int start, int end) {

        int behind() {
            return end + 1;
        }
    }

    private record Attribute(Position keyPosition, Position valuePosition, String key, String value) {

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
                final var attribute = fetchNextAttribute(index);
                if (!attributes.isEmpty()) {
                    final var commaPosition = content.indexOf(',', attributes.getLast().valuePosition().end());
                    Validate.isTrue(() -> commaPosition < attribute.keyPosition().start() && commaPosition != -1, "malformatted json string: missing ',' between fields '%s' and '%s'".formatted(attributes.getLast().key, attribute.key));
                }
                attributes.add(attribute);
                index = attribute.valuePosition().behind();
            }
            return attributes;
        }


        private Attribute fetchNextAttribute(final int index) {
            Position keyPosition = findKey(index);
            Position colonPosition = findColon(keyPosition.behind());
            Position valuePosition = findValue(colonPosition.behind());

            var key = readTextAt(keyPosition);
            var value = readTextAt(valuePosition);
            return new Attribute(keyPosition, valuePosition, key, value);
        }

        private String readTextAt(final Position position) {
            return content.substring(position.start(), position.end());
        }

        private Position findColon(final int index) {
            for (int i = index; i < content.length(); i++) {
                char c = content.charAt(i);
                if (isNonWhitespaceChacter(c)) {
                    if (c == ':') {
                        return new Position(index, index);
                    }
                    throw new IllegalArgumentException("malformatted json string: missing ':'");
                }
            }
            throw new IllegalArgumentException("malformatted json string: attribute without value");
        }

        private static boolean isNonWhitespaceChacter(char character) {
            return !WHITESPACE_CHARS.contains(character);
        }

        private Position findKey(final int index) {
            final var start = content.indexOf('"', index) + 1;
            final var end = content.indexOf('"', start);

            return new Position(start, end);
        }

        private Position findValue(final int index) {
            for (int i = index; i < content.length(); i++) {
                if (content.charAt(i) == '"') {
                    return findQuotedValue(i);
                }
                if (isUnquotedChacater(content.charAt(i))) {
                    return findUnquotedValue(i);
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing value for attribute");
        }

        private static boolean isUnquotedChacater(final char character) {
            return isNonWhitespaceChacter(character) && !FLOW_CHARACTERS.contains(character);
        }

        //TODO Handle different kinds of line feeds
//TODO Handle tabulators
//TODO Handle escaped quotes
        //TODO support .0 values
        //TODO support double values
        //TODO support float values
        private Position findUnquotedValue(final int index) {
            for (int i = index; i < content.length(); i++) {
                if (!isUnquotedChacater(content.charAt(i))) {
                    return new Position(index, i);
                }
            }
            return new Position(index, content.length());
        }

        private Position findQuotedValue(final int index) {
            final var start = content.indexOf('\"', index) + 1;
            final var end = content.indexOf('\"', start + 1);
            Validate.isNotEqual(end, -1, "malformatted json string: missing '\"");
            return new Position(start, end);
        }

        //TODO index all attributes only once
        public <T> T getValue(final Field field) {
            return (T) getAllAttributes().stream()
                .filter(attribute -> attribute.key().equals(field.getName()))
                .findFirst()
                .map(attribute -> toInstance(attribute.value(), field.getType()))
                .orElse(defaultForType(field.getType()));
        }

        private <T> T toInstance(final String value, final Class<T> type) {
            if (type.isEnum()) {
                return (T) Enum.valueOf((Class<Enum>) type, value);
            }
            return switch (type.getSimpleName()) {
                case "String" -> (T) value;
                case "Integer", "int" -> (T) Integer.valueOf(value);
                case "Boolean", "boolean" -> (T) Boolean.valueOf(value);
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
