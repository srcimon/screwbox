package io.github.simonbas.screwbox.core.savegame.internal;

import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.savegame.Savegame;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.scenes.Scenes;
import io.github.simonbas.screwbox.core.utils.TimeoutCache;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.simonbas.screwbox.core.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;

public class DefaultSavegame implements Savegame {

    private final Scenes scenes;
    private final TimeoutCache<String, Boolean> savegameCache = new TimeoutCache<>(ofSeconds(1));

    public DefaultSavegame(final Scenes scenes) {
        this.scenes = scenes;
    }

    @Override
    public Savegame create(final String name) {
        return create(name, scenes.activeScene());
    }

    @Override
    public Savegame create(final String name, final Class<? extends Scene> scene) {
        verifyName(name);
        requireNonNull(scene, "scene must not be null");
        final Entities entities = scenes.entitiesOf(scene);
        final List<Entity> allEntities = entities.allEntities();
        try (OutputStream outputStream = new FileOutputStream(name)) {
            try (OutputStream zippedOutputStream = new GZIPOutputStream(outputStream)) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(zippedOutputStream)) {
                    objectOutputStream.writeObject(allEntities);
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("could not save entities", e);
        }
        return this;
    }

    @Override
    public Savegame load(final String name) {
        return load(name, scenes.activeScene());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Savegame load(final String name, final Class<? extends Scene> scene) {
        verifyName(name);
        requireNonNull(scene, "scene must not be null");
        final Entities entities = scenes.entitiesOf(scene);
        entities.clearEntities();
        try (InputStream inputStream = new FileInputStream(name)) {
            try (InputStream zippedInputStream = new GZIPInputStream(inputStream)) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(zippedInputStream)) {
                    final var allEntities = (List<Entity>) objectInputStream.readObject();
                    entities.add(allEntities);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not load entities", e);
        }
        return this;
    }

    @Override
    public boolean exists(final String name) {
        verifyName(name);
        return savegameCache.getOrElse(name, () -> Files.exists(Path.of(name)));
    }

    @Override
    public Savegame delete(String name) {
        try {
            verifyName(name);
            final Path path = Path.of(name);
            Files.delete(path);
            savegameCache.clear(name);
        } catch (IOException e) {
            throw new IllegalStateException("could not delete savegame: " + name, e);
        }
        return this;
    }

    private void verifyName(final String name) {
        requireNonNull(name, "name must not be null");
    }

}