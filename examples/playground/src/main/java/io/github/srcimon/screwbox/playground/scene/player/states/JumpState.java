package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.JumpControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;

public class JumpState implements EntityState { ;

private Time time = Time.now();
    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SoundBundle.JUMP);
        entity.get(DashControlComponent.class).isEnabled = true;
        entity.get(JumpControlComponent.class).isEnabled = false;
        entity.get(ClimbComponent.class).isEnabled = false;
        entity.get(WallJumpComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).isEnabled = false;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(engine.loop().time().isAfter(time.addMillis(20))) {
            return new FallState();
        }
        return this;
    }
}
