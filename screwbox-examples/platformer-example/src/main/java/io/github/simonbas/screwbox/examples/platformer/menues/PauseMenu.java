package io.github.simonbas.screwbox.examples.platformer.menues;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.examples.platformer.scenes.GameScene;
import io.github.simonbas.screwbox.examples.platformer.scenes.StartScene;

public class PauseMenu extends UiMenu {

    private static final String SAVEGAME_NAME = "savegame.sav";

    public PauseMenu() {
        addItem("Resume game").onActivate(this::resumeGame);

        addItem("Save Game").onActivate(engine -> {
            engine.savegame().create(SAVEGAME_NAME, GameScene.class);
            resumeGame(engine);
        });

        addItem("Load Game").onActivate(engine -> {
            engine.savegame().load(SAVEGAME_NAME, GameScene.class);
            resumeGame(engine);
        }).activeCondition(engine -> engine.savegame().exists(SAVEGAME_NAME));

        addItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu(new PauseMenu())));
        addItem("Back to menu").onActivate(engine -> engine.scenes().switchTo(StartScene.class));
        addItem("Quit Game").onActivate(Engine::stop);
    }

    private void resumeGame(Engine engine) {
        engine.ui().closeMenu();
        engine.scenes().switchTo(GameScene.class);
    }
}
