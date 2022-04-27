package de.suzufa.screwbox.playground.debo.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.menues.StartGameMenu;
import de.suzufa.screwbox.playground.debo.systems.StartBackgroundSystem;

public class StartScene implements Scene {

    @Override
    public void initialize(EntityEngine entityEngine) {
        entityEngine.add(new StartBackgroundSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new StartGameMenu());
    }

}
