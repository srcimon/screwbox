package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import java.util.List;

import static java.lang.Math.atan2;
import static java.util.Objects.isNull;

public class SoftBodyShapeSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.of(SoftBodyShapeComponent.class, SoftBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var body : engine.environment().fetchAll(BODIES)) {
            var softBody = body.get(SoftBodyComponent.class);
            var config = body.get(SoftBodyShapeComponent.class);
            if (isNull(config.shape)) {
                config.shape = softBody.shape;
            }

            var motionToCenter = softBody.shape.center().substract(config.shape.center());
            double degrees = calculateRotation(softBody.shape)-calculateRotation(config.shape);
            var correctionRotation = Angle.degrees(degrees);
            for (var node : config.shape.nodes()) {
                var newEnd = correctionRotation.applyOn(Line.between(config.shape.center(),  node)).end();
                engine.graphics().world().drawCircle(newEnd.add(motionToCenter), 2, OvalDrawOptions.outline(Color.GREEN).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            }
        }
    }

    private double calculateRotation(Polygon shape) {
        return calculateAverageAngle(shape.center(), shape.nodes()).degrees();
    }

    //TODO move to angle
    public static Angle calculateAverageAngle(final Vector origin, List<Vector> others) {
        double sumSin = 0;
        double sumCos = 0;

        for (Vector p : others) {
            // Step 1: Get the angle of the point relative to the center
            double angle = Math.atan2(p.y() - origin.y(), p.x() - origin.x());

            // Step 2: Sum the unit vector components
            sumSin += Math.sin(angle);
            sumCos += Math.cos(angle);
        }

        // Step 3: Convert the summed vector back into an angle
        // Math.atan2 returns the result in radians (-PI to PI)
        return Angle.degrees(Math.toDegrees(Math.atan2(sumSin, sumCos)));
    }
}
