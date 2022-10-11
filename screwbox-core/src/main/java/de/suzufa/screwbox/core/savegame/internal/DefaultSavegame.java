package de.suzufa.screwbox.core.savegame.internal;

import static java.util.Objects.requireNonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.savegame.Savegame;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.core.scenes.Scenes;

public class DefaultSavegame implements Savegame {

    private final Scenes scenes;

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
        final Path path = Path.of(name);
        return Files.exists(path);
    }

    @Override
    public Savegame delete(String name) {
        try {
            verifyName(name);
            final Path path = Path.of(name);
            Files.delete(path);
        } catch (IOException e) {
            throw new IllegalStateException("could not delete savegame: " + name, e);
        }
        return this;
    }

    private void verifyName(final String name) {
        requireNonNull(name, "name must not be null");
    }

}