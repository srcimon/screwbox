package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import dev.screwbox.core.keyboard.Key;

public class OldschoolModeSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.Y)) {
            engine.environment().toggleSystem(new CrtMonitorOverlaySystem());
        }
    }
}
