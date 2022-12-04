package de.suzufa.screwbox.examples.platformer.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.examples.platformer.menues.StartGameMenu;
import de.suzufa.screwbox.examples.platformer.systems.StartBackgroundSystem;

public class StartScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        entities.add(new StartBackgroundSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new StartGameMenu());
    }

}
