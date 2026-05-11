package dev.screwbox.tiled.internal;

import dev.screwbox.core.utils.Json;
import dev.screwbox.core.utils.Resources;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@Deprecated
//TODO Json.loadFromFile()
public class JsonLoader {

    private JsonLoader() {

    }

    public static <T> T loadJson(final String fileName, final Class<T> type) {
        requireNonNull(fileName, "file name must not be null");
        requireNonNull(type, "type must not be null");

        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException(fileName + " is not a JSON-File");
        }
        final var binaryContent = Resources.loadBinary(fileName);
        final var textContent = new String(binaryContent, UTF_8);
        return Json.load(textContent, type);
    }
}
