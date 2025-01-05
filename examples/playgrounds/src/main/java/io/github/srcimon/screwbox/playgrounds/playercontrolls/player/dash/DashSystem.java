package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class DashSystem implements EntitySystem {

    private static final Archetype DASHERS = Archetype.of(DashComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(PlayerControls.DASH)) {
            for (final var entity : engine.environment().fetchAll(DASHERS)) {
                var dashConfig = entity.get(DashComponent.class);
                if (dashConfig.dashStarted.isUnset()) {
                    dashConfig.dashStarted = engine.loop().time();

                    final var physics = entity.get(PhysicsComponent.class);

                 //   physics.momentum = engine.keyboard().arrowKeysMovement(200);//TODO USE KEYS
                }
            }
        }
    }
}
