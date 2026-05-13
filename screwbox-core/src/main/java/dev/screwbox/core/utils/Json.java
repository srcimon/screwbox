package dev.screwbox.core.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * An ultra simple Json object converter.
 *
 * @since 3.29.0
 */
//TODO document all restrictions
public class Json {

    private static final int NOT_FOUND = -1;
    private static final String LIST = "java.util.List";
    private static final String STRING = "java.lang.String";
    private static final String INTEGER = "java.lang.Integer";
    private static final String INT = "int";
    private static final String DOUBLE = "java.lang.Double";
    private static final String DOUBLE_PRIMITIVE = "double";
    private static final String FLOAT = "java.lang.Float";
    private static final String FLOAT_PRIMITVE = "float";
    private static final String BOOLEAN = "java.lang.Boolean";
    private static final String BOOLEAN_PRIMITIVE = "boolean";

    private static final Set<Character> WHITESPACE_CHARS = Set.of(' ', '\t', '\n', '\r');
    private static final Set<Character> JSON_CHARS = Set.of(':', '{', '}', ',', '"');

    private record Position(int start, int end) {

        int after() {
            return end + 1;
        }
    }

    private record Attribute(Position keyPosition, Position valuePosition, String key, String value) {

    }

    private static class JsonObject {

        private final List<Attribute> attributes = new ArrayList<>();

        private JsonObject(final String content) {
            var trimmedContent = content.trim();
            Validate.isTrue(() -> trimmedContent.startsWith("{"), "input is no json string: " + trimmedContent);
            Validate.isTrue(() -> trimmedContent.endsWith("}"), "input is no json string: " + trimmedContent);
            final var pureContent = trimmedContent.substring(1, trimmedContent.length() - 1).trim();
            initializeAttributes(pureContent);
        }

        private void initializeAttributes(final String content) {
            int index = 0;
            while (index < content.length()) {
                final var attribute = fetchNextAttribute(content, index);
                if (!attributes.isEmpty()) {
                    final var commaPosition = content.indexOf(',', attributes.getLast().valuePosition().end());
                    Validate.isTrue(() -> commaPosition < attribute.keyPosition().start() && commaPosition != NOT_FOUND, "malformatted json string: missing ',' between fields '%s' and '%s'".formatted(attributes.getLast().key, attribute.key));
                }
                attributes.add(attribute);
                index = attribute.valuePosition().after();
            }
        }

        private static Attribute fetchNextAttribute(final String content, final int index) {
            final Position keyPosition = findKey(content, index);
            final Position colonPosition = findColon(content, keyPosition.after());
            final Position valuePosition = findValue(content, colonPosition.after());

            var key = content.substring(keyPosition.start(), keyPosition.end());
            var value = content.substring(valuePosition.start(), valuePosition.end());
            return new Attribute(keyPosition, valuePosition, key, value);
        }

        private static Position findColon(final String content, final int index) {
            for (int i = index; i < content.length(); i++) {
                final char character = content.charAt(i);
                if (isNonWhitespaceChacter(character)) {
                    if (character == ':') {
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

        private static Position findKey(final String content, final int index) {
            final var start = content.indexOf('"', index) + 1;
            final var end = content.indexOf('"', start);

            return new Position(start, end);
        }

        private static Position findValue(final String content, final int index) {
            for (int i = index; i < content.length(); i++) {
                char character = content.charAt(i);
                if (character == '"') {
                    return findQuotedValue(content, i);
                }
                if (character == '{') {
                    return findChildValue(content, i);
                }
                if (character == '[') {
                    return findArrayValue(content, i);
                }
                if (isUnquotedChacater(character)) {
                    return findUnquotedValue(content, i);
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing value for attribute");
        }

        private static Position findArrayValue(final String value, final int index) {
            int stackLevel = 0;
            for (int i = index; i < value.length(); i++) {
                if (value.charAt(i) == '[') {
                    stackLevel++;
                } else if (value.charAt(i) == ']') {
                    stackLevel--;
                    if (stackLevel == 0) {
                        return new Position(index + 1, i);
                    }
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing ']'");
        }

        private static boolean isUnquotedChacater(final char character) {
            return isNonWhitespaceChacter(character) && !JSON_CHARS.contains(character);
        }

        //TODO blog on zero dependencies
        //TODO include zero dependencies within documentation (580kb less dependencies)
        //TODO Handle escaped quotes
        //TODO support .0 values
        private static Position findUnquotedValue(final String value, final int index) {
            for (int i = index; i < value.length(); i++) {
                if (!isUnquotedChacater(value.charAt(i))) {
                    return new Position(index, i);
                }
            }
            return new Position(index, value.length());
        }

        private static Position findChildValue(final String value, final int index) {
            int stackLevel = 0;
            for (int i = index; i < value.length(); i++) {
                if (value.charAt(i) == '{') {
                    stackLevel++;
                } else if (value.charAt(i) == '}') {
                    stackLevel--;
                    if (stackLevel == 0) {
                        return new Position(index, i + 1);
                    }
                }
            }
            throw new IllegalArgumentException("malformatted json string: missing '}'");
        }

        private static Position findQuotedValue(final String value, final int index) {
            final var start = value.indexOf('\"', index) + 1;
            final var end = value.indexOf('\"', start);
            Validate.isNotEqual(end, NOT_FOUND, "malformatted json string: missing '\"");
            return new Position(start, end);
        }

        public <T> T getValue(final Field field) {
            String cleanedFieldName = field.getName().endsWith("_") ? field.getName().substring(0, field.getName().length() - 1) : field.getName();
            return (T) attributes.stream()
                .filter(attribute -> attribute.key().equals(cleanedFieldName))
                .findFirst()
                .map(attribute -> toInstance(attribute.value(), field))
                .orElse(defaultForType(field.getType()));
        }

        private Object toInstance(final String value, final Field field) {
            final Class<?> type = field.getType();
            return LIST.equals(type.getName())
                ? deserializeList(value, field)
                : toInstance(value, type);
        }

        //TODO support arrays
        @SuppressWarnings("unchecked")
        private Object toInstance(final String value, final Class<?> type) {
            if (type.isEnum()) {
                return Enum.valueOf((Class<Enum>) type, value);
            }
            return switch (type.getName()) {
                case STRING -> value.replace("\\/", "/");
                case INTEGER, INT -> Integer.valueOf(value.trim());
                case DOUBLE, DOUBLE_PRIMITIVE -> Double.valueOf(value.trim());
                case FLOAT, FLOAT_PRIMITVE -> Float.valueOf(value.trim());
                case BOOLEAN, BOOLEAN_PRIMITIVE -> Boolean.valueOf(value.trim());
                default -> load(value, type);
            };
        }

        private static <T> T defaultForType(final Class<?> type) {
            return switch (type.getName()) {
                case INTEGER, INT -> (T) Integer.valueOf(0);
                case FLOAT, FLOAT_PRIMITVE -> (T) Float.valueOf(0.0f);
                case DOUBLE, DOUBLE_PRIMITIVE -> (T) Double.valueOf(0.0);
                case BOOLEAN, BOOLEAN_PRIMITIVE -> (T) Boolean.FALSE;
                case LIST -> (T) new ArrayList<>();
                default -> null;
            };
        }

        private Object deserializeList(final String value, final Field field) {
            final var type = findGenericTypeOfListField(field);

            return switch (type.getName()) {
                case INTEGER, INT, FLOAT, FLOAT_PRIMITVE, DOUBLE, DOUBLE_PRIMITIVE,
                     BOOLEAN, BOOLEAN_PRIMITIVE -> splitPrimitiveList(value, type);
                //TODO handle strings
                default -> splitObjectList(value, type);
            };
        }


        private Object splitObjectList(final String value, final Class<?> type) {
            final var list = new ArrayList<>();
            int startIndex = 0;
            while (startIndex < value.length() && value.indexOf('{', startIndex) != NOT_FOUND) {
                final var position = findChildValue(value, startIndex);
                list.add(toInstance(value.substring(position.start, position.end), type));
                startIndex = position.after();
            }
            return list;
        }

        private List<Object> splitPrimitiveList(final String value, final Class<?> type) {
            final var list = new ArrayList<>();
            if (value.contains(",")) {
                for (var element : value.split(",")) {
                    list.add(toInstance(element, type));
                }
            }
            return list;
        }

        private static Class<?> findGenericTypeOfListField(final Field field) {
            try {
                final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                final var elementType = parameterizedType.getActualTypeArguments()[0];
                return Class.forName(elementType.getTypeName());
            } catch (final ClassNotFoundException e) {
                throw new IllegalArgumentException("could not find generic type of list", e);
            }
        }
    }

    public static <T> T loadFile(final String fileName, final Class<T> type) {
        requireNonNull(fileName, "file name must not be null");
        requireNonNull(type, "type must not be null");

        Validate.isTrue(() -> fileName.toLowerCase().endsWith(".json"), fileName + " is not a JSON-File");
        final var binaryContent = Resources.loadBinary(fileName);
        final var textContent = new String(binaryContent, UTF_8);
        return load(textContent, type);
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
            return constructor.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("could not instantiate type " + type.getSimpleName(), e);
        }
    }
}
