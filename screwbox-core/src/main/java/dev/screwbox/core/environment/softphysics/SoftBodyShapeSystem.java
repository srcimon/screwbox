package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
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

                var motionToCenter = softBody.shape.center().substract(config.shape.center());
                Angle correctionRotation = config.isRotationAllowed ?
                        calculateRotation(config.shape, softBody.shape) : Angle.none();
                for (int nodeNr = 0; nodeNr < config.shape.definitionNotes().size(); nodeNr++) {
                    var node = config.shape.definitionNotes().get(nodeNr);
                    var newEnd = correctionRotation.applyOn(Line.between(config.shape.center(), node)).end().add(motionToCenter);
                    Entity jointTarget = softBody.nodes.get(nodeNr);
                    final Vector delta = jointTarget.position().substract(newEnd);
                    final double distance = delta.length();
                    if (delta.length() > config.deadZone) {//TODO configure dead zone
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

    private Angle calculateRotation(Polygon shape, Polygon other) {
        Double lastDiff = null;
        double totalCumulativeRotation = 0;
        for (int i = 0; i < shape.nodes().size(); i++) {
            double angleA = Math.atan2(shape.node(i).y() - shape.center().y(), shape.node(i).x() - shape.center().x());
            double angleB = Math.atan2(other.node(i).y() - other.center().y(), other.node(i).x() - other.center().x());

            double currentDiff = angleB - angleA;

            while (currentDiff <= -Math.PI) currentDiff += 2 * Math.PI;
            while (currentDiff > Math.PI) currentDiff -= 2 * Math.PI;

            if (nonNull(lastDiff)) {
                if (currentDiff - lastDiff > Math.PI) {
                    currentDiff -= 2 * Math.PI;
                } else if (currentDiff - lastDiff < -Math.PI) {
                    currentDiff += 2 * Math.PI;
                }
            }

            lastDiff = currentDiff;
            totalCumulativeRotation += currentDiff;

        }
        double averageRotationRadians = totalCumulativeRotation / (double) shape.nodes().size();

        // Normalize the *final average* to the standard range if desired
        while (averageRotationRadians <= -Math.PI) averageRotationRadians += 2 * Math.PI;
        while (averageRotationRadians > Math.PI) averageRotationRadians -= 2 * Math.PI;

        return Angle.degrees(Math.toDegrees(averageRotationRadians));
    }
}