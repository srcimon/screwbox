package io.github.simonbas.screwbox.examples.pathfinding.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.scenes.Scene;

//TODO: Start any Engine with Loading Scene?
public class LoadingScene implements Scene {

    @Override
    public void initialize(Entities entities) {
        entities.add(engine -> {
            if (engine.loop().isWarmedUp()) {
                engine.scenes().switchTo(DemoScene.class);
            }
        });
    }

    @Override
    public void onEnter(Engine engine) {
        engine.ui().showLoadingAnimation();
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().hideLoadingAnimation();
    }
}
