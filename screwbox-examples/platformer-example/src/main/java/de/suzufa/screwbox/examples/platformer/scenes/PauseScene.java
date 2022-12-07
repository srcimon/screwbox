package de.suzufa.screwbox.examples.platformer.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.examples.platformer.components.BackgroundHolderComponent;
import de.suzufa.screwbox.examples.platformer.menues.PauseMenu;
import de.suzufa.screwbox.examples.platformer.systems.GetSreenshotOfGameSceneSystem;
import de.suzufa.screwbox.examples.platformer.systems.RenderPauseScreenshotSystem;

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
