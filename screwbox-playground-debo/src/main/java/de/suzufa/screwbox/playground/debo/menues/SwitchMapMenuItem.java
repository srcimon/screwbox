package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class SwitchMapMenuItem extends UiMenuItem {

    public SwitchMapMenuItem(final String label, final String map) {
        super(label);
        onActivate(engine -> {
            engine.ui().closeMenu();
            engine.scenes().add(new GameScene(map));
            engine.scenes().switchTo(GameScene.class);
        });
    }

}
