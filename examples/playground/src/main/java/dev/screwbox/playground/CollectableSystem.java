package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.TargetLockComponent;
import dev.screwbox.core.environment.ai.TargetMovementComponent;

public class CollectableSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var target = engine.environment().fetchById(1);
        for(var entity : engine.environment().fetchAllHaving(CollectableComponent.class)) {
            if(entity.position().distanceTo(target.position()) < 50) {
                entity.remove(CollectableComponent.class);
                entity.add(new TargetMovementComponent(target.position()));
            }

        }
    }
}
