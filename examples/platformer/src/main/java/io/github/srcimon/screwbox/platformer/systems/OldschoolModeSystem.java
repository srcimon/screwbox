package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.CrtMonitorOverlaySystem;
import io.github.srcimon.screwbox.core.graphics.ShaderBundle;
import io.github.srcimon.screwbox.core.keyboard.Key;

import static java.util.Objects.nonNull;

public class OldschoolModeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.Y)) {
            boolean hasShader = nonNull(engine.graphics().configuration().overlayShader());
            if (hasShader) {
                engine.graphics().configuration().disableOverlayShader();
                engine.environment().remove(CrtMonitorOverlaySystem.class);
            } else {
                engine.graphics().configuration().setOverlayShader(ShaderBundle.GRAYSCALE);
                engine.environment().addSystem(new CrtMonitorOverlaySystem());
            }
        }
    }
}
