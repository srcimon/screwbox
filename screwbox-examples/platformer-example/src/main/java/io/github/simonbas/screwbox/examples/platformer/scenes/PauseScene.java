package io.github.simonbas.screwbox.examples.platformer.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.examples.platformer.components.BackgroundHolderComponent;
import io.github.simonbas.screwbox.examples.platformer.menues.PauseMenu;
import io.github.simonbas.screwbox.examples.platformer.systems.GetSreenshotOfGameSceneSystem;
import io.github.simonbas.screwbox.examples.platformer.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
    }

    @Override
    public void initialize(Entities entities) {
        entities.add(new GetSreenshotOfGameSceneSystem())
                .add(new RenderPauseScreenshotSystem())
                .add(new Entity()
                        .add(new BackgroundHolderComponent()));
    }

}
