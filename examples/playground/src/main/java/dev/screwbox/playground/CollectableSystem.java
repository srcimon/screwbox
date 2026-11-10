package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.TargetLockComponent;
import dev.screwbox.core.environment.ai.TargetMovementComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class CollectableSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var target = engine.environment().fetchById(1);
        for(var entity : engine.environment().fetchAllHaving(CollectableComponent.class)) {
            double distance = entity.position().distanceTo(target.position());
            if(distance < 20) {
                if(!entity.hasComponent(TargetMovementComponent.class)) {
                    entity.add(new TargetMovementComponent(target.position()));
                } else {
                    entity.get(TargetMovementComponent.class).position = target.position();
                }
            } else if(distance > 40){
                entity.get(PhysicsComponent.class).velocity = Vector.zero();
                entity.removeIfPresent(TargetMovementComponent.class);
            }
            if(entity.position().distanceTo(target.position()) < 4) {
                engine.environment().remove(entity);
                engine.audio().playSound(SoundBundle.PLING, SoundOptions.playOnce().position(target.position()));
            }
        }
    }
}
