package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class WallJumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(WallJumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(PlayerControls.JUMP)) {
            for (final var entity : engine.environment().fetchAll(JUMPERS)) {
                var wallIsLeft = entity.get(CollisionDetailsComponent.class).touchesLeft;
                var jumpConfig = entity.get(WallJumpComponent.class);
                if (jumpConfig.jumpStarted.isUnset()) {
                    jumpConfig.jumpStarted = engine.loop().time();
                    final var physics = entity.get(PhysicsComponent.class);
                    physics.momentum = Vector.$((wallIsLeft ? 1 : -1) * 100, -200);
                }
            }
        }
    }
}
