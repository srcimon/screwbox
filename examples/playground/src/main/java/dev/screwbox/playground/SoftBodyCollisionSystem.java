package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftbodyCollisionComponent.class);

    record Check(Entity first, Entity second) {

    }

    @Override
    public void update(Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final Set<Check> checks = new HashSet<>();
        for(final var body : bodies) {
            for (final var other : bodies) {
                if(!body.equals(other) && !checks.contains(new Check(other, body))) {
                    checks.add(new Check(body, other));
                }
            }
        }

        for(final var check : checks) {
            Polygon firstPoly = toPolygon(check.first);
            Polygon secondPoly = toPolygon(check.second);
            drawInsidePoints(engine, firstPoly, secondPoly, check.first, check.second);
            drawInsidePoints(engine, secondPoly, firstPoly, check.first, check.second);
        }
    }

    record PolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder, Consumer<Line> moveSegment) {

    }
    private static void drawInsidePoints(Engine engine, Polygon first, Polygon second, Entity firstEntity, Entity secondEntity) {
        for(final var node : first.nodes()) {
            if(second.contains(node)) {
                Line closest  = second.segments().getFirst();
                double distance = closest.closestPoint(node).distanceTo(node);//TODO new method -> closestDistance
                int nodeNr = 0;
                for(int i = 1; i < second.segments().size(); i++) {
                    var segment = second.segments().get(i);
                    double currentDistance = segment.closestPoint(node).distanceTo(node);
                    if(currentDistance < distance) {
                        closest = segment;
                        distance = currentDistance;
                        nodeNr = i;
                    }
                }
                var body = secondEntity.get(SoftBodyComponent.class);
                final var fn = body.nodes.get(nodeNr);
                final var fn2 = body.nodes.get(nodeNr+1);
                var collision = new PolygonCollision(node, closest, p -> firstEntity.moveTo(p), p -> {
                    fn.moveTo(p.start());
                    fn2.moveTo(p.start());
                });
                engine.graphics().world().drawCircle(collision.intruder, 2, OvalDrawOptions.filled(Color.MAGENTA).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
                engine.graphics().world().drawLine(collision.segment, LineDrawOptions.color(Color.MAGENTA).strokeWidth(3).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
            }
        }
    }

    private static Polygon toPolygon(Entity entity) {
        return Polygon.ofNodes(entity.get(SoftBodyComponent.class).nodes.stream().map(Entity::position).toList());
    }
}
