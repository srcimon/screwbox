package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

@ExecutionOrder(Order.SIMULATION_EARLY)
public class SoftPhysicsSystem implements EntitySystem {

    private static final Archetype LINKS = Archetype.ofSpacial(SoftLinkComponent.class);
    private static final Archetype STRUCTURES = Archetype.ofSpacial(SoftStructureComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(LINKS)) {
            final var link = entity.get(SoftLinkComponent.class);
            updateLink(entity, link, engine);
        }

        for (final var entity : engine.environment().fetchAll(STRUCTURES)) {
            final var structure = entity.get(SoftStructureComponent.class);
            for (int index = 0; index < structure.targetIds.length; index++) {
                final var link = new SoftLinkComponent(structure.targetIds[index]);
                link.length = structure.lengths[index];
                link.flexibility = structure.flexibility;
                link.expand = structure.expand;
                link.retract = structure.retract;
                updateLink(entity, link, engine);
                structure.lengths[index] = link.length;
            }
        }
    }

    private static void updateLink(final Entity linkEntity, final SoftLinkComponent link, final Engine engine) {
        final var jointTarget = engine.environment().fetchById(link.targetId);
        final double distance = linkEntity.position().distanceTo(jointTarget.position());
        if (link.length == 0) {
            link.length = distance;
        }
        final Vector delta = jointTarget.position().substract(linkEntity.position());
        final boolean isRetracted = distance - link.length > 0;
        final double strength = isRetracted ? link.retract : link.expand;
        final Vector motion = delta.limit(link.flexibility).multiply((distance - link.length) * engine.loop().delta() * strength);
        final var physics = linkEntity.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            physics.velocity = physics.velocity.add(motion);
        }
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        if (nonNull(targetPhysics)) {
            targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        }
        link.angle = Angle.ofVector(delta);
    }

}
