package dev.screwbox.platformer.scenes;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.platformer.menues.PauseMenu;
import dev.screwbox.platformer.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
    }

    @Override
    public void populate(Environment environment) {
        environment
                .enableRendering()
                .addSystem(new RenderPauseScreenshotSystem());
    }

}
