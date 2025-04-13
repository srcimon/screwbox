package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.platformer.scenes.StartScene;

public class BackToMenuSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.ESCAPE)) {
            engine.scenes().switchTo(StartScene.class, SceneTransition.custom()
                    .introDurationMillis(500));
        }
    }

}
