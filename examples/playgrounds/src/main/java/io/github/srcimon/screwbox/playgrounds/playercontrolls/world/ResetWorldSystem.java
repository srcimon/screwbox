package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class ResetWorldSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        if(engine.keyboard().isPressed(PlayerControls.RESET)) {
            engine.scenes().resetActiveScene();
        }
    }
}
