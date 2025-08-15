package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class JointsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.environment().fetchAllHaving(JointComponent.class).forEach(o -> {
            var joint = o.get(JointComponent.class);
            var physics = o.get(PhysicsComponent.class);
            var jointTarget = engine.environment().fetchById(joint.targetEntityId);
            double distance = o.position().distanceTo(jointTarget.position());
            if (joint.length == 0) {
                joint.length = distance;
            }
            Vector delta = jointTarget.position().substract(o.position());
            Vector motion = delta.multiply((joint.length - distance) * engine.loop().delta());
            physics.momentum = physics.momentum.add(motion);
        });
    }
}
