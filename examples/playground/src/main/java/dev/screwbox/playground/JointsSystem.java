package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class JointsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.environment().fetchAllHaving(JointComponent.class).forEach(o -> {
            var jointEntity = o.get(JointComponent.class);
            for (var joint : jointEntity.joints) {
                var physics = o.get(PhysicsComponent.class);
                var jointTarget = engine.environment().fetchById(joint.targetEntityId);
                var targetPhysics = jointTarget.get(PhysicsComponent.class);
                double distance = o.position().distanceTo(jointTarget.position());
                if (joint.length == 0) {
                    joint.length = distance;
                }
                Vector delta = jointTarget.position().substract(o.position());
                var strength = distance > joint.length ? joint.strength / 2.0 : joint.strength;

                Vector motion = delta.multiply(-1 * (joint.length - distance) * engine.loop().delta() * strength);
                physics.momentum = physics.momentum.add(motion);
                targetPhysics.momentum = targetPhysics.momentum.add(motion.invert())
                        .applyFriction(engine.loop().delta(80), engine.loop().delta(80));

            }
        });
    }
}
