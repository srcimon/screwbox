package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.systems.PhysicsDebugSystem;
import io.github.simonbas.screwbox.core.keyboard.Key;

public class DebugConfigSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().justPressed(Key.O)) {
            engine.entities().toggleSystem(new PhysicsDebugSystem());
        }
        if (engine.keyboard().justPressed(Key.L)) {
            engine.entities().toggleSystem(new ShowFpsSystem());
            engine.loop().setTargetFps(engine.entities().isSystemPresent(ShowFpsSystem.class) ? 120 : 10000);
        }
    }
}
