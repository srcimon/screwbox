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
            jointLink.angle = Angle.ofVector(updateJoint(engine, linkEntity, jointLink.link));
        }

        for (final var structureEntity : engine.environment().fetchAll(STRUCTURES)) {
            final var jointStructure = structureEntity.get(JointStructureComponent.class);
            for (final var joint : jointStructure.links) {
                updateJoint(engine, structureEntity, joint);
            }
        }
    }

    private static Vector updateJoint(final Engine engine, final Entity jointEntity, final Joint joint) {
        final var physics = jointEntity.get(PhysicsComponent.class);
        final var jointTarget = engine.environment().fetchById(joint.targetEntityId());
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        final double distance = jointEntity.position().distanceTo(jointTarget.position());
        if (joint.restLength == 0) {
            joint.restLength = distance;
        }
        Vector delta = jointTarget.position().substract(jointEntity.position());
        boolean isRetracted = distance - joint.restLength > 0;
        double strength = isRetracted ? joint.retractStrength : joint.expandStrength;

        final Vector motion = delta.limit(20).multiply((distance - joint.restLength) * engine.loop().delta() * strength);//TODO joint.stiffness = 20
        physics.velocity = physics.velocity.add(motion);
        targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        return delta;
    }
}
