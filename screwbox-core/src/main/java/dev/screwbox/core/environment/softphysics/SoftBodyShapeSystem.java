package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
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

                final var polygonShift = softBody.shape.center().substract(config.shape.center());
                final var polygonRotation = config.isRotationAllowed
                        ? config.shape.averageRotationDifferenceTo(softBody.shape)
                        : Angle.none();
                for (int nodeNr = 0; nodeNr < config.shape.definitionNotes().size(); nodeNr++) {
                    var node = config.shape.definitionNotes().get(nodeNr);
                    var newEnd = polygonRotation.applyOn(Line.between(config.shape.center(), node)).end().add(polygonShift);
                    Entity jointTarget = softBody.nodes.get(nodeNr);
                    final Vector delta = jointTarget.position().substract(newEnd);
                    final double distance = delta.length();
                    if (delta.length() > config.deadZone) {
                        final Vector motion = delta.limit(config.flexibility).multiply(distance * engine.loop().delta() * config.strength);
                        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
                        if (nonNull(targetPhysics)) {
                            targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
                        }
                    }
                }
            }
        }
    }
}