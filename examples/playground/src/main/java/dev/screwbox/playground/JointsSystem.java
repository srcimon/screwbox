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
                engine.environment().tryFetchById(joint.targetEntityId).ifPresent(jointTarget -> {
                    var targetPhysics = jointTarget.get(PhysicsComponent.class);
                    double distance = o.position().distanceTo(jointTarget.position());
                    if (joint.length == 0) {
                        joint.length = distance;
                    }
                    Vector delta = jointTarget.position().substract(o.position());

                    Vector motion = delta.multiply(-1 * (joint.length - distance) * engine.loop().delta() * joint.strength);
                    physics.momentum = physics.momentum.add(motion);
                    targetPhysics.momentum = targetPhysics.momentum.add(motion.invert())
                            .applyFriction(engine.loop().delta(200), engine.loop().delta(200));

                });
            }
        });
    }
}
