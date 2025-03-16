package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.keyboard.Key;

import static java.util.Objects.nonNull;

public class OldschoolModeSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final boolean hasShader = nonNull(engine.graphics().configuration().overlayShader());
        final boolean crtSystemPresent = engine.environment().isSystemPresent(CrtMonitorOverlaySystem.class);

        if (engine.keyboard().isPressed(Key.Y)) {
            if (hasShader) {
                engine.graphics().configuration().disableOverlayShader();
            } else {
                engine.graphics().configuration().setOverlayShader(ShaderBundle.GAME_BOY);
            }
        }
        if (hasShader != crtSystemPresent) {
           engine.environment().toggleSystem(new CrtMonitorOverlaySystem());
        }
    }
}
