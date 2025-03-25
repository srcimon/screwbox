package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class OldschoolModeSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.Y)) {
            engine.environment().toggleSystem(new CrtMonitorOverlaySystem());
        }
    }
}
