package io.github.simonbas.screwbox.examples.platformer.menues;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.examples.platformer.scenes.GameScene;

public class StartGameMenu extends UiMenu {

    public StartGameMenu() {
        addItem("Start Tutorial").onActivate(engine -> startMap("maps/0-1_intro.json", engine));
        addItem("Start Level 1").onActivate(engine -> startMap("maps/1-1_teufelsinsel.json", engine));
        addItem("Start Level 2").onActivate(engine -> startMap("maps/1-2_misty_caves.json", engine));
        addItem("continue").onActivate(engine -> {
            engine.scenes().add(new GameScene());
            engine.savegame().load("savegame.sav", GameScene.class);
            engine.scenes().switchTo(GameScene.class);
            engine.ui().closeMenu();
        }).activeCondition(engine -> engine.savegame().exists("savegame.sav"));

        addItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu(new StartGameMenu())));
        addItem("Quit").onActivate(Engine::stop);
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
