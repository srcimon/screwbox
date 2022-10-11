package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class StartGameMenu extends UiMenu {

    public StartGameMenu() {
        add(new UiMenuItem("Start Tutorial").onActivate(engine -> startMap("maps/0-1_intro.json", engine)));
        add(new UiMenuItem("Start Level 1").onActivate(engine -> startMap("maps/1-1_teufelsinsel.json", engine)));
        add(new UiMenuItem("Start Level 2").onActivate(engine -> startMap("maps/1-2_misty_caves.json", engine)));
        add(new UiMenuItem("continue").onActivate(engine -> {
            engine.scenes().add(new GameScene());
            engine.savegame().load("savegame.sav", GameScene.class);
            engine.scenes().switchTo(GameScene.class);
            engine.ui().closeMenu();
        }).activeCondition(engine -> engine.savegame().exists("savegame.sav")));

        add(new UiMenuItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu(new StartGameMenu()))));
        add(new UiMenuItem("Quit").onActivate(Engine::stop));
    }

    @Override
    public void onExit(Engine engine) {
        engine.stop();
    }

    private void startMap(String map, Engine engine) {
        engine.ui().closeMenu();
        engine.scenes().add(new GameScene(map));
        engine.scenes().switchTo(GameScene.class);
    }
}
