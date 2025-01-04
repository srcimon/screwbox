package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class JumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(JumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(PlayerControls.JUMP)) {
            for (final var entity : engine.environment().fetchAll(JUMPERS)) {
                var jumpConfig = entity.get(JumpComponent.class);
                if (jumpConfig.jumpStarted.isUnset()) {
                    jumpConfig.jumpStarted = engine.loop().time();
                    final var physics = entity.get(PhysicsComponent.class);
                    physics.momentum = physics.momentum.addY(-210);
                }
            }
        }
    }

}
