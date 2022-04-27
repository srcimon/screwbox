package de.suzufa.screwbox.core.utils;

import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public final class ResourceLoader {

    private ResourceLoader() {
    }

    public static File resourceFile(final String fileName) {
        Objects.requireNonNull(fileName, "fileName must not be null");
        final URL url = ResourceLoader.class.getClassLoader().getResource(fileName);
        if (isNull(url)) {
            throw new IllegalArgumentException("file not found: " + fileName);
        }
        return new File(url.getFile());
    }

    public static byte[] loadResource(final String fileName) {
        final File resourceFile = resourceFile(fileName);
        try {
            return Files.readAllBytes(resourceFile.toPath());
        } catch (final IOException e) {
            throw new IllegalArgumentException("file could not be read: " + fileName);
        }
    }

}
