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
                Entities entities = engine.scenes().entitiesOf(GameScene.class);
                serialize(entities, "savegame.sav");
                new PauseMenuResumeGame().onActivate(engine);
            }

        });
        add(new UiMenuItem("Load Game") {

            @Override
            public void onActivate(Engine engine) {
                Entities entities = engine.scenes().entitiesOf(GameScene.class);
                entities.clearEntities();
                addAllFrom(entities, "savegame.sav");
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

    private void addAllFrom(Entities entities, String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            try (ObjectInputStream oos = new ObjectInputStream(fis)) {
                var allEntities = (List<Entity>) oos.readObject();
                entities.add(allEntities);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not deserialize file.", e);
        }
    }

    private void serialize(Entities entities, String filename) {
        List<Entity> allEntities = entities.allEntities();
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(allEntities);
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not serialize entities.", e);
        }
    }

}
