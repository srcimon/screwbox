package io.github.srcimon.screwbox.examples.platformer.scenes;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundHolderComponent;
import io.github.srcimon.screwbox.examples.platformer.menues.PauseMenu;
import io.github.srcimon.screwbox.examples.platformer.systems.GetSreenshotOfGameSceneSystem;
import io.github.srcimon.screwbox.examples.platformer.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
        engine.window().setTitle("Platformer (Pause)");
    }

    @Override
    public void populate(Environment environment) {
        environment
                .addSystem(new GetSreenshotOfGameSceneSystem())
                .addSystem(new RenderPauseScreenshotSystem())
                .addEntity(new BackgroundHolderComponent());
    }

}
