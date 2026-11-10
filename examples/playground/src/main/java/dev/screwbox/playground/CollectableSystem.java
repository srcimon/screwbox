package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.TargetMovementComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class CollectableSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var target = engine.environment().fetchById(1);
        for (var entity : engine.environment().fetchAllHaving(CollectableComponent.class)) {
            double distance = entity.position().distanceTo(target.position());
            if (distance < 30 && !entity.hasComponent(TargetMovementComponent.class)) {
                entity.add(new TargetMovementComponent(target.forceId(), 600), c -> c.overshootSpeedModifier = 20);
            } else if (distance > 40) {
                entity.get(PhysicsComponent.class).velocity = Vector.zero();
                entity.remove(TargetMovementComponent.class);
            }
            if (entity.position().distanceTo(target.position()) < 4) {
                engine.environment().remove(entity);
                engine.audio().playSound(SoundBundle.PLING, SoundOptions.playOnce().position(target.position()));
            }
        }
    }
}
