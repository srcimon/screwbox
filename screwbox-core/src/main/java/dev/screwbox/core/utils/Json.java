package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//TODO move inside tiled?
public class Json {

    private static final Set<Character> WHITESPACE_CHARS = Set.of(' ', '\t', '\n', '\r');
    private static final Set<Character> FLOW_CHARACTERS = Set.of(':', '{', '}', ',', '"');

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
            Validate.isTrue(() -> trimmedContent.startsWith("{"), "input is no json string: " + trimmedContent);
            Validate.isTrue(() -> trimmedContent.endsWith("}"), "input is no json string: " + trimmedContent);
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

            var key = content.substring(keyPosition.start(), keyPosition.end());
            var value = content.substring(valuePosition.start(), valuePosition.end());
            System.out.println("--> " + value);

            return new Attribute(keyPosition, valuePosition, key, value);
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
                char character = content.charAt(i);
                if (character == '"') {
                    return findQuotedValue(i);
                }
                if (character == '{') {
                    return findChildValue(i);
                }
                if (character == '[') {
                    return findArrayValue(i);
                }

                if (isUnquotedChacater(character)) {
                    return findUnquotedValue(i);
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing value for attribute");
        }

        private Position findArrayValue(final int index) {
            for (int i = index; i < content.length(); i++) {
                if (content.charAt(i) == ']') {
                    return new Position(index + 1, i);
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing ']'");
        }

        private static boolean isUnquotedChacater(final char character) {
            return isNonWhitespaceChacter(character) && !FLOW_CHARACTERS.contains(character);
        }

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

        private Position findChildValue(int index) {
            int stackLevel = 0;
            for (int i = index; i < content.length(); i++) {
                if (content.charAt(i) == '{') {
                    stackLevel++;
                }
                if (content.charAt(i) == '}') {
                    stackLevel--;
                    if (stackLevel == 0) {
                        return new Position(index, i + 1);
                    }
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing '}'");
        }

        private Position findQuotedValue(final int index) {
            final var start = content.indexOf('\"', index) + 1;
            final var end = content.indexOf('\"', start);
            Validate.isNotEqual(end, -1, "malformatted json string: missing '\"");
            return new Position(start, end);
        }

        //TODO index all attributes only once
        public <T> T getValue(final Field field) {
            return (T) getAllAttributes().stream()
                .filter(attribute -> attribute.key().equals(field.getName()))
                .findFirst()
                .map(attribute -> toInstance(attribute.value(), field))
                .orElse(defaultForType(field.getType()));
        }

        private Object toInstance(final String value, final Field field) {
            final Class<?> type = field.getType();
            if ("java.util.List".equals(type.getName())) {
                return deserializeList(value, field);
            }
            return toInstance(value, type);
        }

        private Object toInstance(final String value, final Class<?> type) {
            if (type.isEnum()) {
                return Enum.valueOf((Class<Enum>) type, value);
            }
            return switch (type.getName()) {
                case "java.lang.String" -> value;
                case "java.lang.Integer", "int" -> Integer.valueOf(value);
                case "java.lang.Boolean", "boolean" -> Boolean.valueOf(value);
                default -> load(value, type);
            };
        }

        private Object deserializeList(final String value, final Field field) {
            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final var elementType = parameterizedType.getActualTypeArguments()[0];
            try {
                final var list = new ArrayList<>();
                if (value.contains(", ")) {
                    String[] split = value.split(", ");
                    for (var element : split) {
                        list.add(toInstance(element, Class.forName(elementType.getTypeName())));

                    }
                }
                return list;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("FIXM E", e);
            }

        }

        private static <T> T defaultForType(final Class<?> type) {
            return switch (type.getName()) {
                case "java.lang.Integer", "int" -> (T) Integer.valueOf(0);
                case "java.lang.Boolean", "boolean" -> (T) Boolean.FALSE;
                case "java.util.List" -> (T) new ArrayList<>();
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
