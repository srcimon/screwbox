package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static dev.screwbox.core.environment.Order.SIMULATION_LATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ExecutionOrder(SIMULATION_LATE)
public class SoftBodyShapeSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.of(SoftBodyShapeComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            final var softBody = body.get(SoftBodyComponent.class);
            if (nonNull(softBody.shape)) {
                final var config = body.get(SoftBodyShapeComponent.class);
                if (isNull(config.shape)) {
                    config.shape = softBody.shape;
                }
                applyForceOnSoftBodyNodesToPreserveShape(config, softBody, engine.loop().delta());
            }
        }
    }

    private static void applyForceOnSoftBodyNodesToPreserveShape(final SoftBodyShapeComponent config, final SoftBodyComponent softBody, final double delta) {
        final var fittedTemplate = softBody.shape.alignTemplate(config.shape, config.isRotationAllowed);
        for (int nodeNr = 0; nodeNr < config.shape.definitionNotes().size(); nodeNr++) {
            var newEnd = fittedTemplate.definitionNotes().get(nodeNr);
            final Entity linkTarget = softBody.nodes.get(nodeNr);
            final Vector shift = linkTarget.position().substract(newEnd);
            final double shiftDistance = shift.length();
            if (shift.length() > config.deadZone) {
                final Vector motion = shift.limit(config.flexibility).multiply(shiftDistance * delta * config.strength);
                final var targetPhysics = linkTarget.get(PhysicsComponent.class);
                if (nonNull(targetPhysics)) {
                    targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
                }
            }
        }
    }
}