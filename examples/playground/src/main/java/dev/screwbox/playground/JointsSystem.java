package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;

@Order(Order.SystemOrder.PREPARATION)
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

                    boolean isRetracted = distance-joint.length < 0;
                    double strength = isRetracted ? joint.retractStength : joint.expandStrength;

                    Vector motion = delta.multiply(-1 * (joint.length - distance) * engine.loop().delta() * strength);
                        physics.momentum = physics.momentum.applyFriction(engine.loop().delta(50), engine.loop().delta(50)).add(motion);
                        targetPhysics.momentum = targetPhysics.momentum.applyFriction(engine.loop().delta(50), engine.loop().delta(50)).add(motion.invert());
                });
            }
        });
    }
}
