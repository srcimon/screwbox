package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

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
            var correctionRotation = Angle.degrees( calculateRotation(softBody.shape)-calculateRotation(config.shape));
            for (var node : config.shape.nodes()) {
                var newEnd = correctionRotation.applyOn(Line.between(config.shape.center(),  node)).end();
                engine.graphics().world().drawCircle(newEnd.add(motionToCenter), 4, OvalDrawOptions.filled(Color.GREEN).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            }
        }


    }

    private double calculateRotation(Polygon shape) {
        double degrees = 0;
        for (final var node : shape.nodes()) {
            degrees += Angle.of(Line.between(shape.center(), node)).degrees();
        }
        return degrees / shape.nodes().size();
    }
}
