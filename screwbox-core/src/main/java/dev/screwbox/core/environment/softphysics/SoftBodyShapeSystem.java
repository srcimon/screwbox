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
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ExecutionOrder(Order.SIMULATION_EARLY)
public class SoftBodyShapeSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.of(SoftBodyShapeComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            var softBody = body.get(SoftBodyComponent.class);
            if (softBody.shape != null) {
                var config = body.get(SoftBodyShapeComponent.class);
                if (isNull(config.shape)) {
                    config.shape = softBody.shape;
                }

                var motionToCenter = softBody.shape.center().substract(config.shape.center());
                Angle correctionRotation = calculateRotation(config.shape, softBody.shape);
                for (int nodeNr = 0; nodeNr < config.shape.definitionNotes().size(); nodeNr++) {
                    var node = config.shape.definitionNotes().get(nodeNr);
                    var newEnd = correctionRotation.applyOn(Line.between(config.shape.center(), node)).end().add(motionToCenter);
                    SoftLinkComponent link = new SoftLinkComponent(0);
                    link.expand = 20;
                    link.retract = 20;
                    link.flexibility = 30;
                    updateLink(newEnd, softBody.nodes.get(nodeNr), link, engine);
                    engine.graphics().world().drawCircle(newEnd, 2, OvalDrawOptions.outline(Color.GREEN).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                }
            }
        }
    }


    private Angle calculateRotation(Polygon shape, Polygon other) {
        double totalRotation = 0;
        Double lastDiff = null;
        double totalCumulativeRotation = 0;
        for (int i = 0; i < shape.nodes().size(); i++) {
            double angleA = Math.atan2(shape.node(i).y() - shape.center().y(), shape.node(i).x() - shape.center().x());
            double angleB = Math.atan2(other.node(i).y() - other.center().y(), other.node(i).x() - other.center().x());

            double currentDiff = angleB - angleA;

            // Normalize the *initial* difference to the shortest path (-PI to PI]
            while (currentDiff <= -Math.PI) currentDiff += 2 * Math.PI;
            while (currentDiff > Math.PI) currentDiff -= 2 * Math.PI;

            if (lastDiff != null) {
                // Check if the current diff is significantly different from the last,
                // suggesting we wrapped around the +/- PI boundary *during* the loop.
                // If the jump between adjacent nodes is > PI, adjust currentDiff to align with the average direction.
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

    //TODO duplication
    private static void updateLink(final Vector position, Entity jointTarget, final SoftLinkComponent link, final Engine engine) {
        final double distance = position.distanceTo(jointTarget.position());
        final Vector delta = jointTarget.position().substract(position);
        engine.graphics().world().drawLine(Line.between(position, position.add(delta)), LineDrawOptions.color(Color.BLUE).strokeWidth(4).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
        final boolean isRetracted = distance - link.length > 0;
        final double strength = isRetracted ? link.retract : link.expand;
        final Vector motion = delta.limit(link.flexibility).multiply((distance - link.length) * engine.loop().delta() * strength);
        final var targetPhysics = jointTarget.get(PhysicsComponent.class);
        if (nonNull(targetPhysics)) {
            targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
        }
    }
}
