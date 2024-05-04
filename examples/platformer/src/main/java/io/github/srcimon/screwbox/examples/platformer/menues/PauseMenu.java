package io.github.srcimon.screwbox.examples.platformer.menues;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;
import io.github.srcimon.screwbox.examples.platformer.scenes.StartScene;

import static io.github.srcimon.screwbox.core.scenes.AnimationBundle.COLOR_FADE;

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
        addItem("Back to menu").onActivate(engine -> engine.scenes().switchTo(StartScene.class, SceneTransition
                .extroAnimation(COLOR_FADE)
                .extroDurationMillis(250)
                .extroEase(Ease.SINE_IN)
                .introAnimation(COLOR_FADE)
                .introDurationMillis(250)
                .introEase(Ease.SINE_OUT)));
        addItem("Quit Game").onActivate(Engine::stop);
    }

    private void resumeGame(Engine engine) {
        engine.ui().closeMenu();
        engine.scenes().switchTo(GameScene.class);
    }
}
