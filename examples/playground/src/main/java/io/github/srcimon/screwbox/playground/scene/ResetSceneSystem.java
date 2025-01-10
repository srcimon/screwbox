package io.github.srcimon.screwbox.playground.scene;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.playground.scene.player.ControlKeys;

public class ResetSceneSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(ControlKeys.RESET)) {
            engine.scenes().resetActiveScene();
        }
    }
}
