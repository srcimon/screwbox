package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsDebugSystem;
import de.suzufa.screwbox.core.keyboard.Key;

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
