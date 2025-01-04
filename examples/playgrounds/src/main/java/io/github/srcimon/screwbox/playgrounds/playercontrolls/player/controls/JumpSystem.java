package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class JumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(JumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(PlayerControls.JUMP)) {
            for (final var entity : engine.environment().fetchAll(JUMPERS)) {
                var jumpConfig = entity.get(JumpComponent.class);
                final Time jumLockTime = engine.loop().time().addMillis(-350);
                if (jumpConfig.remainingJumps > 0 && jumpConfig.last.isBefore(jumLockTime)) {
                    jumpConfig.remainingJumps -= 1;
                    jumpConfig.last = engine.loop().time();
                    final var physics = entity.get(PhysicsComponent.class);
                    physics.momentum = physics.momentum.addY(-210);
                }
            }
        }
    }

}
