package de.suzufa.screwbox.playground.debo.menues;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class PauseMenu extends UiMenu {

    public PauseMenu() {
        add(new PauseMenuResumeGame());
        add(new UiMenuItem("Save Game") {

            @Override
            public void onActivate(Engine engine) {
                List<Entity> allEntities = engine.scenes().entitiesOf(GameScene.class).allEntities();
                serialize(allEntities, "savegame.sav");
                new PauseMenuResumeGame().onActivate(engine);
            }

        });
        add(new UiMenuItem("Load Game") {

            @Override
            public void onActivate(Engine engine) {
                List<Entity> allEntities = deserialize("savegame.sav");
                Entities entities = engine.scenes().entitiesOf(GameScene.class);
                for (Entity entity : entities.allEntities()) {
                    entities.remove(entity);
                }
                entities.add(allEntities);
                new PauseMenuResumeGame().onActivate(engine);

            }

        });
        add(new UiMenuItem("Options") {

            @Override
            public void onActivate(Engine engine) {
                engine.ui().openMenu(new OptionsMenu(new PauseMenu()));
            }
        });
        add(new UiMenuItem("Back to menu") {

            @Override
            public void onActivate(Engine engine) {
                engine.scenes().switchTo(StartScene.class);
            }
        });
        add(new UiMenuItem("Quit Game") {

            @Override
            public void onActivate(Engine engine) {
                engine.stop();
            }
        });
    }

    private List<Entity> deserialize(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            try (ObjectInputStream oos = new ObjectInputStream(fis)) {
                return (List<Entity>) oos.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Could not deserialize File.", e);
        }
    }

    private void serialize(List<Entity> entities, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(entities);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not serialize Entities.", e);
        }
    }

}
