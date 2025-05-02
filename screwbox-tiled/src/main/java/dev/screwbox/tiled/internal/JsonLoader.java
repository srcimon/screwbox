package dev.screwbox.tiled.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.screwbox.core.utils.Resources;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class JsonLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonLoader() {
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
}
