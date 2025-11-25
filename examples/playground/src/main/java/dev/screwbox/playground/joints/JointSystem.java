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
        for (final var linkEntity : engine.environment().fetchAll(LINKS)) {
            final var jointLink = linkEntity.get(JointLinkComponent.class);
            updateJoint(linkEntity, jointLink, engine);
        }

        for (final var structureEntity : engine.environment().fetchAll(STRUCTURES)) {
            final var jointStructure = structureEntity.get(JointStructureComponent.class);
            for (int index = 0; index < jointStructure.targetIds.length; index++) {
                final var link = new JointLinkComponent(jointStructure.targetIds[index]);
                link.length = jointStructure.lengths[index];
                link.flexibility = jointStructure.flexibility;
                link.expand = jointStructure.expand;
                link.retract = jointStructure.retract;
                updateJoint(structureEntity, link, engine);
                jointStructure.lengths[index] = link.length;
            }
        }
    }

    private static void updateJoint(final Entity jointEntity, final JointLinkComponent joint, Engine engine) {
        final var jointTarget = engine.environment().fetchById(joint.targetId);
        final double distance = jointEntity.position().distanceTo(jointTarget.position());
        if (joint.length == 0) {
            joint.length = distance;
        }
        final Vector delta = jointTarget.position().substract(jointEntity.position());
        final boolean isRetracted = distance - joint.length > 0;
        final double strength = isRetracted ? joint.retract : joint.expand;
        final Vector motion = delta.limit(joint.flexibility).multiply((distance - joint.length) * engine.loop().delta() * strength);
        final var physics = jointEntity.get(PhysicsComponent.class);
        physics.velocity = physics.velocity.add(motion);
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        joint.angle = Angle.ofVector(delta);
    }

}
