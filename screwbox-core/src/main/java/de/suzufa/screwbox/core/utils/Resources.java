package de.suzufa.screwbox.core.utils;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Resources {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Resources() {
    }

    @Deprecated
    // TODO: Replace
    public static File loadFile(final String fileName) {
        Objects.requireNonNull(fileName, "fileName must not be null");
        final URL url = Resources.class.getClassLoader().getResource(fileName);
        if (isNull(url)) {
            throw new IllegalArgumentException("file not found: " + fileName);
        }
        return new File(url.getFile());
    }

    public static byte[] loadBinary(final String fileName) {
        final File resourceFile = loadFile(fileName);
        try {
            return Files.readAllBytes(resourceFile.toPath());
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be read: " + fileName);
        }
    }

    // TODO: Test
    public static <T> T loadJson(final String fileName, final Class<T> type) {
        requireNonNull(fileName, "fileName must not be null");
        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException(fileName + " is not a JSON-File");
        }
        // TODO: check Type not null
        try {
            final File file = Resources.loadFile(fileName);
            return OBJECT_MAPPER.readValue(file, type);
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be deserialized: " + fileName, e);
        }
    }

}
