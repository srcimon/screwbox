package dev.screwbox.tiled.internal;

import dev.screwbox.core.utils.Resources;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import static java.util.Objects.requireNonNull;

public class JsonLoader {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        .build();

    private JsonLoader() {

    }

    public static <T> T loadJson(final String fileName, final Class<T> type) {
        requireNonNull(fileName, "file name must not be null");
        requireNonNull(type, "type must not be null");

        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException(fileName + " is not a JSON-File");
        }
        final var fileContent = Resources.loadBinary(fileName);
        try {
            return JSON_MAPPER.readValue(fileContent, type);
        } catch (final JacksonException e) {
            throw new IllegalArgumentException("file could not be deserialized: " + fileName, e);
        }
    }
}
