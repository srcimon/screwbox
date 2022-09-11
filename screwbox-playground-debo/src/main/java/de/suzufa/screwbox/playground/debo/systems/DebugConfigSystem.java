package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.systems.PhysicsDebugSystem;
import de.suzufa.screwbox.core.keyboard.Key;

public class DebugConfigSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().justPressed(Key.O)) {
            switchSystem(engine, new PhysicsDebugSystem());
        }
        if (engine.keyboard().justPressed(Key.L)) {
            switchSystem(engine, new ShowFpsSystem());
            engine.loop().setTargetFps(engine.entities().isSystemPresent(ShowFpsSystem.class) ? 120 : 10000);
        }
    }

    private void switchSystem(final Engine engine, final EntitySystem system) {
        if (engine.entities().isSystemPresent(system.getClass())) {
            engine.entities().remove(system.getClass());
        } else {
            engine.entities().add(system);
        }
    }
}
