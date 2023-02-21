package io.github.simonbas.screwbox.examples.platformer.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.examples.platformer.menues.StartGameMenu;
import io.github.simonbas.screwbox.examples.platformer.systems.StartBackgroundSystem;

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
