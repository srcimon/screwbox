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
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import static dev.screwbox.core.environment.Order.DEBUG_OVERLAY_LATE;
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
//TODO store snapPolygon in ShapeComponent
                final var snapPolygon = softBody.shape.matchTemplate(config.shape, config.isRotationAllowed);
                for (int nodeNr = 0; nodeNr < config.shape.definitionNotes().size(); nodeNr++) {
                    var newEnd = snapPolygon.definitionNotes().get(nodeNr);
                    engine.graphics().world().drawCircle(newEnd, 2, OvalDrawOptions.filled(Color.WHITE).drawOrder(DEBUG_OVERLAY_LATE.drawOrder()));
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