package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Ecosphere;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.menues.StartGameMenu;
import io.github.srcimon.screwbox.examples.platformer.systems.StartBackgroundSystem;

public class StartScene implements Scene {

    @Override
    public void populate(Ecosphere ecosphere) {
        ecosphere.addSystem(new StartBackgroundSystem());
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new StartGameMenu());
        engine.window().setTitle("Platformer Example (Menu)");
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().closeMenu();
    }
}
