package de.suzufa.screwbox.core.utils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides convenience methods to access {@link ClassLoader} accessible
 * resources. Used to import packed resouces into your game.
 */
public final class Resources {

    private static final ClassLoader CLASS_LOADER = Resources.class.getClassLoader();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Resources() {
    }

    /**
     * Returns true if there is a resource with the given file name.
     */
    public static boolean resourceExists(final String fileName) {
        final URL url = fileUrl(fileName);
        return nonNull(url);
    }

    public static byte[] loadBinary(final String fileName) {
        requireNonNull(fileName, "fileName must not be null");
        try (InputStream resourceStream = CLASS_LOADER.getResourceAsStream(fileName)) {
            if (isNull(resourceStream)) {
                throw new IllegalArgumentException("file not found: " + fileName);
            }
            return resourceStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("resource could not be read: " + fileName, e);
        }
    }

    public static <T> T loadJson(final String fileName, final Class<T> type) {
        requireNonNull(fileName, "fileName must not be null");
        requireNonNull(type, "type must not be null");

        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException(fileName + " is not a JSON-File");
        }
        try {
            final var fileContent = Resources.loadBinary(fileName);
            return OBJECT_MAPPER.readValue(fileContent, type);
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be deserialized: " + fileName, e);
        }
    }

    private static URL fileUrl(final String fileName) {
        Objects.requireNonNull(fileName, "fileName must not be null");
        return CLASS_LOADER.getResource(fileName);
    }
}
