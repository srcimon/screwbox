package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
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
            final var jointTarget = engine.environment().fetchById(jointLink.targetId);
            updateJoint(jointTarget, linkEntity, jointLink, deltaTime);
        }

        for (final var structureEntity : engine.environment().fetchAll(STRUCTURES)) {
            final var jointStructure = structureEntity.get(JointStructureComponent.class);
            for (int index = 0; index < jointStructure.targetIds.length; index++) {
                final var jointTarget = engine.environment().fetchById(jointStructure.targetIds[index]);
                final var mapped = new JointLinkComponent(jointStructure.targetIds[index]);
                mapped.length = jointStructure.lengths[index];
                mapped.stiffness = jointStructure.stiffness;
                mapped.expand = jointStructure.expand;
                mapped.retract = jointStructure.retract;
                updateJoint(jointTarget, structureEntity, mapped, deltaTime);
                jointStructure.lengths[index] = mapped.length;
            }
        }
    }

    private static void updateJoint(final Entity jointTarget, final Entity jointEntity, final JointLinkComponent joint, double deltaTime) {
        final double distance = jointEntity.position().distanceTo(jointTarget.position());
        if (joint.length == 0) {
            joint.length = distance;
        }
        Vector delta = jointTarget.position().substract(jointEntity.position());
        final boolean isRetracted = distance - joint.length > 0;
        final double strength = isRetracted ? joint.retract : joint.expand;
        final Vector motion = delta.limit(joint.stiffness).multiply((distance - joint.length) * deltaTime * strength);
        final var physics = jointEntity.get(PhysicsComponent.class);
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        physics.velocity = physics.velocity.add(motion);
        targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        joint.angle = Angle.ofVector(delta);
    }

}
