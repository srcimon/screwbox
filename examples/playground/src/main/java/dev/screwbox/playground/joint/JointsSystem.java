package dev.screwbox.playground.joint;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;

@ExecutionOrder(Order.PREPARATION)
public class JointsSystem implements EntitySystem {

    private static final Archetype JOINTS = Archetype.ofSpacial(LinkJointComponent.class);
    private static final Archetype INTEGRITY_JOINTS = Archetype.ofSpacial(IntegrityJointsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var jointEntity : engine.environment().fetchAll(JOINTS)) {
            final var joint = jointEntity.get(LinkJointComponent.class);
            updateJoint(engine, jointEntity, joint.joint);
        }

        for (final var jointEntity : engine.environment().fetchAll(INTEGRITY_JOINTS)) {
            final var integrityJoints = jointEntity.get(IntegrityJointsComponent.class);
            for (final var joint : integrityJoints.joints) {
                updateJoint(engine, jointEntity, joint);
            }
        }
    }

    private static void updateJoint(Engine engine, Entity jointEntity, Joint joint) {
        var physics = jointEntity.get(PhysicsComponent.class);
        engine.environment().tryFetchById(joint.targetEntityId).ifPresent(jointTarget -> {
            var targetPhysics = jointTarget.get(PhysicsComponent.class);
            double distance = jointEntity.position().distanceTo(jointTarget.position());
            if (joint.restLength == 0) {
                joint.restLength = distance;
            }
            Vector delta = jointTarget.position().substract(jointEntity.position());
            joint.angle = Angle.ofVector(delta);
            boolean isRetracted = distance - joint.restLength > 0;
            double strength = isRetracted ? joint.retractStrength : joint.expandStrength;

            final Vector motion = delta.limit(20).multiply((distance - joint.restLength) * engine.loop().delta() * strength);//TODO joint.stiffness = 20
            physics.velocity = physics.velocity.add(motion);
            targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        });
    }
}
