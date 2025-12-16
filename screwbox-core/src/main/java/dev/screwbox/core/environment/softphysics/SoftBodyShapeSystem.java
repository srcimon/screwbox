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

import java.util.List;

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
                for (int nodeNr = 0; nodeNr <  config.shape.nodes().size(); nodeNr++) {
                    var node = config.shape.nodes().get(nodeNr);
                    var newEnd = correctionRotation.applyOn(Line.between(config.shape.center(), node)).end().add(motionToCenter);
                    SoftLinkComponent link = new SoftLinkComponent(0);
                    link.expand = 10;
                    link.retract = 10;
                    link.flexibility = 10;
                    updateLink(newEnd, softBody.nodes.get(nodeNr+1), link, engine);//TODO +1 WTF?
                    engine.graphics().world().drawCircle(newEnd, 2, OvalDrawOptions.outline(Color.GREEN).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                }
                //TODO see https://www.youtube.com/watch?v=3OmkehAJoyo&t=563s
            }
        }
    }


    private Angle calculateRotation(Polygon shape, Polygon other) {
        double totalRotation = 0;


        for (int i = 0; i < shape.nodes().size(); i++) {
            double angleA = Math.atan2(shape.node(i).y() - shape.center().y(),
                    shape.node(i).x() - shape.center().x());
            double angleB = Math.atan2(other.node(i).y() - other.center().y(),
                    other.node(i).x() - other.center().x());

            double diff = angleB - angleA;
            while (diff <= -Math.PI) diff += 2 * Math.PI;
            while (diff > Math.PI) diff -= 2 * Math.PI;

            totalRotation += diff;
        }
        return Angle.degrees(Math.toDegrees((totalRotation / (double) shape.nodes().size())));
    }

    //TODO duplication
    private static void updateLink(final Vector position, Entity jointTarget, final SoftLinkComponent link, final Engine engine) {
        final double distance = position.distanceTo(jointTarget.position());
        final Vector delta = jointTarget.position().substract(position);
        engine.graphics().world().drawLine(Line.between(position, position.add(delta)), LineDrawOptions.color(Color.BLUE).strokeWidth(4).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
        if(delta.length() > 22) {
            final boolean isRetracted = distance - link.length > 0;
            final double strength = isRetracted ? link.retract : link.expand;
            final Vector motion = delta.limit(link.flexibility).multiply((distance - link.length) * engine.loop().delta() * strength);
            final var targetPhysics = jointTarget.get(PhysicsComponent.class);
            if (nonNull(targetPhysics)) {
                targetPhysics.velocity = targetPhysics.velocity.add(motion.invert());
            }
        }
    }
}
