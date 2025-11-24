package dev.screwbox.playground.joints;

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
public class JointSystem implements EntitySystem {

    private static final Archetype LINKS = Archetype.ofSpacial(JointLinkComponent.class);
    private static final Archetype STRUCTURES = Archetype.ofSpacial(JointStructureComponent.class);

    @Override
    public void update(final Engine engine) {
        double deltaTime = engine.loop().delta();

        for (final var linkEntity : engine.environment().fetchAll(LINKS)) {
            final var jointLink = linkEntity.get(JointLinkComponent.class);
            final var jointTarget = engine.environment().fetchById(jointLink.link.targetEntityId());
            updateJoint(jointTarget, linkEntity, jointLink.link, deltaTime);
        }

        for (final var structureEntity : engine.environment().fetchAll(STRUCTURES)) {
            final var jointStructure = structureEntity.get(JointStructureComponent.class);
            for (final var joint : jointStructure.links) {
                final var jointTarget = engine.environment().fetchById(joint.targetEntityId());
                updateJoint(jointTarget, structureEntity, joint, deltaTime);
            }
        }
    }

    private static void updateJoint(final Entity jointTarget, final Entity jointEntity, final Joint joint, double deltaTime) {
        final double distance = jointEntity.position().distanceTo(jointTarget.position());
        if (joint.restLength == 0) {
            joint.restLength = distance;
        }
        Vector delta = jointTarget.position().substract(jointEntity.position());
        boolean isRetracted = distance - joint.restLength > 0;
        double strength = isRetracted ? joint.retract : joint.expand;


        final Vector motion = delta.limit(joint.stiffness).multiply((distance - joint.restLength) * deltaTime * strength);
        final var physics = jointEntity.get(PhysicsComponent.class);
        physics.velocity = physics.velocity.add(motion);
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        joint.angle = Angle.ofVector(delta);
    }
}
