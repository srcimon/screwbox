package de.suzufa.screwbox.tiled.internal;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public final class EntityLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private EntityLoader() {
    }

    public static <T> T load(final String fileName, final Class<T> type) {
        try {
            final File file = ResourceLoader.resourceFile(fileName);
            return OBJECT_MAPPER.readValue(file, type);
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be deserialized: " + fileName, e);
        }
    }
}
