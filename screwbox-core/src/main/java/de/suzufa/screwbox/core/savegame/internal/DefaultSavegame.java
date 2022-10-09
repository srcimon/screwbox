package de.suzufa.screwbox.core.savegame.internal;

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
import java.util.Objects;
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
        Objects.requireNonNull(name, "name must not be null");
        final Entities entities = scenes.entitiesOf(scene);
        final List<Entity> allEntities = entities.allEntities();
        try (OutputStream fos = new FileOutputStream(name)) {
            try (OutputStream zipOs = new GZIPOutputStream(fos)) {
                try (ObjectOutputStream oos = new ObjectOutputStream(zipOs)) {
                    oos.writeObject(allEntities);
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("could not serialize entities.", e);
        }
        return this;
    }

    @Override
    public Savegame load(String name) {
        return load(name, scenes.activeScene());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Savegame load(String name, Class<? extends Scene> scene) {
        final Entities entities = scenes.entitiesOf(scene);
        entities.clearEntities();
        try (InputStream fis = new FileInputStream(name)) {
            try (InputStream zis = new GZIPInputStream(fis)) {
                try (ObjectInputStream oos = new ObjectInputStream(zis)) {
                    var allEntities = (List<Entity>) oos.readObject();
                    entities.add(allEntities);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not deserialize file.", e);
        }
        return this;
    }

    @Override
    public boolean exists(String name) {
        return Files.exists(Path.of(name));
    }

}
