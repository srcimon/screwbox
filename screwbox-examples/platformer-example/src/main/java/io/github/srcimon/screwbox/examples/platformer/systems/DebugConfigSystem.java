package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.systems.PhysicsDebugSystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class DebugConfigSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.O)) {
            engine.ecosphere().toggleSystem(new PhysicsDebugSystem());
        }
        if (engine.keyboard().isPressed(Key.L)) {
            engine.ecosphere().toggleSystem(new ShowFpsSystem());
            engine.loop().setTargetFps(engine.ecosphere().isSystemPresent(ShowFpsSystem.class) ? 120 : 10000);
        }
    }
}
