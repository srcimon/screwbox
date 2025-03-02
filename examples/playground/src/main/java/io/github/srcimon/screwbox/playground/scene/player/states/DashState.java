package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;

public class DashState implements EntityState {

    private final Time time = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(DashControlComponent.class).isEnabled = false;
        entity.get(LeftRightControlComponent.class).isEnabled = false;
        PhysicsComponent physics = entity.get(PhysicsComponent.class);
        physics.gravityModifier = 0;
        entity.get(ClimbComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).isEnabled = false;
        entity.get(WallJumpComponent.class).isEnabled = false;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return time.addMillis(100).isBefore(engine.loop().time())
                ? new FallState()
                : this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        PhysicsComponent physics = entity.get(PhysicsComponent.class);
        physics.gravityModifier = 1;
    }
}
