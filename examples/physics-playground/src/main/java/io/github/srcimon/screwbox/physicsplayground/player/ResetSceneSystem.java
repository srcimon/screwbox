package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class ResetSceneSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.ENTER)) {
            engine.scenes().resetActiveScene();
        }
    }
}
