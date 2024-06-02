package io.github.srcimon.screwbox.platformer.menues;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.platformer.scenes.GameScene;
import io.github.srcimon.screwbox.platformer.scenes.StartScene;

public class PauseMenu extends UiMenu {

    private static final String SAVEGAME_NAME = "savegame.sav";

    public PauseMenu() {
        addItem("Resume game").onActivate(this::resumeGame);

        addItem("Save Game").onActivate(engine -> {
            engine.scenes().environmentOf(GameScene.class).createSavegame(SAVEGAME_NAME);
            resumeGame(engine);
        });

        addItem("Load Game").onActivate(engine -> {
            engine.scenes().environmentOf(GameScene.class).loadSavegame(SAVEGAME_NAME);
            resumeGame(engine);
        }).activeCondition(engine -> engine.environment().savegameExists(SAVEGAME_NAME));

        addItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu()));
        addItem("Back to menu").onActivate(engine -> engine.scenes().switchTo(StartScene.class, SceneTransition.custom()
                .outroDurationMillis(250)
                .outroEase(Ease.SINE_IN)
                .introDurationMillis(250)
                .introEase(Ease.SINE_OUT)));
        addItem("Quit Game").onActivate(Engine::stop);
    }

    private void resumeGame(Engine engine) {
        engine.ui().closeMenu();
        engine.scenes().switchTo(GameScene.class);
    }
}
