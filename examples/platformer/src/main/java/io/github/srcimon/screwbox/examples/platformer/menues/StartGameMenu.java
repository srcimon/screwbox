package io.github.srcimon.screwbox.examples.platformer.menues;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

public class StartGameMenu extends UiMenu {

    public StartGameMenu() {
        addItem("Start Tutorial").onActivate(engine -> startMap("maps/0-1_intro.json", engine));
        addItem("Start Level 1").onActivate(engine -> startMap("maps/1-1_teufelsinsel.json", engine));
        addItem("Start Level 2").onActivate(engine -> startMap("maps/1-2_misty_caves.json", engine));
        addItem("continue").onActivate(engine -> {
            engine.scenes().add(new GameScene());
            engine.scenes().environmentOf(GameScene.class).loadSavegame("savegame.sav");
            engine.scenes().switchTo(GameScene.class);
            engine.ui().closeMenu();
        }).activeCondition(engine -> engine.environment().savegameExists("savegame.sav"));

        addItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu()));
        addItem("Quit").onActivate(Engine::stop);
    }

    @Override

    public void onExit(Engine engine) {
        engine.stop();
    }

    private void startMap(String map, Engine engine) {
        engine.scenes()
                .addOrReplace(new GameScene(map))
                .switchTo(GameScene.class, SceneTransition.custom()
                        .extroDurationMillis(100)
                        .introAnimation(new CirclesAnimation())
                        .introDurationMillis(1200));
    }
}
