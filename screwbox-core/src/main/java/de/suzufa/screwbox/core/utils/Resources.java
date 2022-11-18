package de.suzufa.screwbox.core.utils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides convenience methods to access {@link ClassLoader} accessible
 * resources. Used to import packed resouces into your game.
 */
public final class Resources {

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
        final URL url = fileUrl(fileName);
        if (isNull(url)) {
            throw new IllegalArgumentException("file not found: " + fileName);
        }

        final File resourceFile = new File(url.getFile());// TODO getResourceAsStream() to load resources in github
                                                          // actions even from other jar
        try {
            return Files.readAllBytes(resourceFile.toPath());
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be read: " + fileName, e);
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
        return Resources.class.getClassLoader().getResource(fileName);
    }
}
