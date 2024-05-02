package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.SceneTransitionBundle;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.examples.platformer.scenes.StartScene;

import static io.github.srcimon.screwbox.core.assets.SceneTransitionBundle.FADEOUT;

public class BackToMenuSystem implements EntitySystem {
//TODO WHEN IS THIS USED?
    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.ESCAPE)) {
            //TODO BUG: Exits engine -> keypress from previous scene is collected? maybe
            engine.scenes().switchTo(StartScene.class, FADEOUT);
        }
    }

}
