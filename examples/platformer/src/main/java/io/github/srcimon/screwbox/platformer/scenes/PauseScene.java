package io.github.srcimon.screwbox.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.platformer.menues.PauseMenu;
import io.github.srcimon.screwbox.platformer.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
    }

    @Override
    public void populate(Environment environment) {
        environment
                .addSystem(new RenderPauseScreenshotSystem());
    }

}
