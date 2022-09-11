package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.components.BackgroundHolderComponent;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class PauseMenuResumeGame extends UiMenuItem {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);

    public PauseMenuResumeGame() {
        super("resume game");
    }

    @Override
    public void onActivate(Engine engine) {
        engine.ui().closeMenu();
        Entity backgroundEntity = engine.entityEngine().forcedFetch(BACKGROUND);
        backgroundEntity.get(BackgroundHolderComponent.class).background = null;
        engine.scenes().switchTo(GameScene.class);
    }
}
