package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class DebugConfigSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.O)) {
            engine.environment().toggleSystem(new PhysicsDebugSystem());
        }
        if (engine.keyboard().isPressed(Key.L)) {
            engine.environment().toggleSystem(new ShowFpsSystem());
            engine.loop().setTargetFps(engine.environment().isSystemPresent(ShowFpsSystem.class) ? 120 : 10000);
        }
    }
}
