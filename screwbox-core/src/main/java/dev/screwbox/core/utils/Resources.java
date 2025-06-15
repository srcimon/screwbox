package dev.screwbox.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Provides convenience methods to access {@link ClassLoader} accessible
 * resources. Used to import packed resources into your game.
 */
public final class Resources {

    private static final ClassLoader CLASS_LOADER = Resources.class.getClassLoader();
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static final String CLASSPATH = System.getProperty("java.class.path", ".");

    private Resources() {
    }

    /**
     * Returns all element names in the classpath.
     */
    public static List<String> classPathElements() {
        final String[] classPathElements = CLASSPATH.split(PATH_SEPARATOR);
        return List.of(classPathElements);
    }

    /**
     * Returns {@code true} if there is a resource with the specified file name.
     */
    public static boolean resourceExists(final String fileName) {
        try (var inputStream = inputStream(fileName)) {
            return nonNull(inputStream);
        } catch (final IOException e) {
            throw new IllegalArgumentException("resource could not be read: " + fileName, e);
        }
    }

    /**
     * Returns the binary content from resource file.
     */
    public static byte[] loadBinary(final String fileName) {
        try (var inputStream = inputStream(fileName)) {
            Validate.isFalse(() -> isNull(inputStream), "file not found: " + fileName);
            return inputStream.readAllBytes();
        } catch (final IOException e) {
            throw new IllegalArgumentException("resource could not be read: " + fileName, e);
        }
    }

    /**
     * Returns a property map from the content of a resource file.
     *
     * @since 3.5.0
     */
    public static Map<String, String> loadProperties(final String fileName) {
        final var propertyMap = new HashMap<String, String>();
        try (var inputStream = Resources.inputStream(fileName)) {
            Validate.isFalse(() -> isNull(inputStream), "file not found: " + fileName);
            final var properties = new Properties();
            properties.load(inputStream);
            for (final var property : properties.entrySet()) {
                propertyMap.put((String) property.getKey(), (String) property.getValue());
            }
            return propertyMap;
        } catch (final IOException e) {
            throw new IllegalArgumentException("properties could not be read: " + fileName, e);
        }
    }

    private static InputStream inputStream(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        return CLASS_LOADER.getResourceAsStream(fileName);
    }

}
