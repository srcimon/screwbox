package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.keyboard.Key;
import io.github.simonbas.screwbox.examples.platformer.scenes.StartScene;

public class BackToMenuSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.ESCAPE)) {
            engine.scenes().switchTo(StartScene.class);
        }
    }

}
