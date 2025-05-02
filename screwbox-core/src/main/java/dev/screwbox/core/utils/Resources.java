package dev.screwbox.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
     * Returns true if there is a resource with the given file name.
     */
    public static boolean resourceExists(final String fileName) {
        try (var inputStream = getFileInputStream(fileName)) {
            return nonNull(inputStream);
        } catch (final IOException e) {
            throw new IllegalArgumentException("resource could not be read: " + fileName, e);
        }
    }

    public static byte[] loadBinary(final String fileName) {
        try (var inputStream = getFileInputStream(fileName)) {
            if (isNull(inputStream)) {
                throw new IllegalArgumentException("file not found: " + fileName);
            }
            return inputStream.readAllBytes();
        } catch (final IOException e) {
            throw new IllegalArgumentException("resource could not be read: " + fileName, e);
        }
    }

    private static InputStream getFileInputStream(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        return CLASS_LOADER.getResourceAsStream(fileName);
    }
}
