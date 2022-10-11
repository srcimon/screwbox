package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.components.BackgroundHolderComponent;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class PauseMenu extends UiMenu {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);
    private static final String SAVEGAME_NAME = "savegame.sav";

    public PauseMenu() {
        add(new UiMenuItem("Resume game").onActivate(this::resumeGame));

        add(new UiMenuItem("Save Game").onActivate(engine -> {
            engine.savegame().create(SAVEGAME_NAME, GameScene.class);
            resumeGame(engine);
        }));

        add(new UiMenuItem("Load Game").onActivate(engine -> {
            engine.savegame().load(SAVEGAME_NAME, GameScene.class);
            resumeGame(engine);
        }).activeCondition(engine -> engine.savegame().exists(SAVEGAME_NAME)));

        add(new UiMenuItem("Options").onActivate(engine -> engine.ui().openMenu(new OptionsMenu(new PauseMenu()))));
        add(new UiMenuItem("Back to menu").onActivate(engine -> engine.scenes().switchTo(StartScene.class)));
        add(new UiMenuItem("Quit Game").onActivate(Engine::stop));
    }

    private void resumeGame(Engine engine) {
        engine.ui().closeMenu();
        Entity backgroundEntity = engine.entities().forcedFetch(BACKGROUND);
        backgroundEntity.get(BackgroundHolderComponent.class).background = null;
        engine.scenes().switchTo(GameScene.class);
    }
}
