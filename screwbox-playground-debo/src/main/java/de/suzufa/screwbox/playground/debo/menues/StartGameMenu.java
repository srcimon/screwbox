package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class StartGameMenu extends UiMenu {

    public StartGameMenu() {
        add(new SwitchMapMenuItem("Start Tutorial", "maps/0-1_intro.json"));
        add(new SwitchMapMenuItem("Start Level 1", "maps/1-1_teufelsinsel.json"));
        add(new SwitchMapMenuItem("Start Level 2", "maps/1-2_misty_caves.json"));

        add(new UiMenuItem("Load Game") {

            @Override
            public void onActivate(Engine engine) {
                engine.scenes().add(new GameScene(null));
                engine.savegame().load("savegame.sav", GameScene.class);
                engine.scenes().switchTo(GameScene.class);
                engine.ui().closeMenu();

            }

        }.activeCondition(engine -> engine.savegame().exists("savegame.sav")));

        add(new UiMenuItem("Options") {
            @Override
            public void onActivate(Engine engine) {
                engine.ui().openMenu(new OptionsMenu(new StartGameMenu()));

            }
        });
        add(new UiMenuItem("Quit") {

            @Override
            public void onActivate(Engine engine) {
                engine.stop();

            }
        });
    }

    @Override
    public void onExit(Engine engine) {
        engine.stop();
    }
}
