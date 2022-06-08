package de.suzufa.screwbox.playground.debo.scenes;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.scenes.Scene;
import de.suzufa.screwbox.playground.debo.components.BackgroundHolderComponent;
import de.suzufa.screwbox.playground.debo.menues.PauseMenu;
import de.suzufa.screwbox.playground.debo.systems.GetSreenshotOfGameSceneSystem;
import de.suzufa.screwbox.playground.debo.systems.RenderPauseScreenshotSystem;

public class PauseScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        engine.ui().openMenu(new PauseMenu());
    }

    @Override
    public void initialize(EntityEngine entityEngine) {
        entityEngine.add(
                new GetSreenshotOfGameSceneSystem(),
                new RenderPauseScreenshotSystem());

        entityEngine.add(new Entity().add(
                new BackgroundHolderComponent()));
    }

}
