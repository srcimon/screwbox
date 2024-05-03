package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.scenes.StartScene;

import static io.github.srcimon.screwbox.core.scenes.SceneTransitionBundle.FADEOUT;

public class BackToMenuSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.ESCAPE)) {
            engine.scenes().switchTo(StartScene.class, FADEOUT);
        }
    }

}
