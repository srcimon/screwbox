package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class SwitchMapMenuItem extends UiMenuItem {

    private final String map;

    public SwitchMapMenuItem(final String label, final String map) {
        super(label);
        this.map = map;
    }

    @Override
    public void onActivate(Engine engine) {
        engine.ui().closeMenu();
        engine.scenes().add(new GameScene(map));
        engine.scenes().switchTo(GameScene.class);
    }

}
