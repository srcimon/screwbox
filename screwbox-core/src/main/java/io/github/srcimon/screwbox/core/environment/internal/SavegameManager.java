package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.utils.TimeoutCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.srcimon.screwbox.core.Duration.oneSecond;
import static java.util.Objects.requireNonNull;

public class SavegameManager {

    private final TimeoutCache<String, Boolean> savegameCache = new TimeoutCache<>(oneSecond());

    public void createSavegame(List<Entity> entities, final String name) {
        requireNonNull(name, "name must not be null");
        try (final var outputStream = new FileOutputStream(name)) {
            try (final var zippedOutputStream = new GZIPOutputStream(outputStream)) {
                try (final var objectOutputStream = new ObjectOutputStream(zippedOutputStream)) {
                    objectOutputStream.writeObject(entities);
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("could not create savegame: " + name, e);
        }
    }

    public List<Entity> loadSavegame(final String name) {
        validateName(name);
        try (final var inputStream = new FileInputStream(name)) {
            try (final var zippedInputStream = new GZIPInputStream(inputStream)) {
                try (final var objectInputStream = new ObjectInputStream(zippedInputStream)) {
                    return (List<Entity>) objectInputStream.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not load savegame: " + name, e);
        }
    }

    public void deleteSavegame(final String name) {
        try {
            validateName(name);
            final Path path = Path.of(name);
            Files.delete(path);
            savegameCache.clear(name);
        } catch (IOException e) {
            throw new IllegalStateException("could not delete savegame: " + name, e);
        }
    }

    public boolean savegameExists(final String name) {
        validateName(name);
        return savegameCache.getOrElse(name, () -> Files.exists(Path.of(name)));
    }

    private void validateName(final String name) {
        requireNonNull(name, "name must not be null");
        if (name.endsWith(".") || name.endsWith(File.separator)) {
            throw new IllegalArgumentException("savegame name is invalid: " + name);
        }
    }
}
