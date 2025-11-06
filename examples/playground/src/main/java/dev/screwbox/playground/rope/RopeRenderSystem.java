package dev.screwbox.playground.rope;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.playground.joint.JointComponent;

import java.util.ArrayList;
import java.util.List;

@ExecutionOrder(Order.PRESENTATION_WORLD)
public class RopeRenderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeRenderComponent.class, JointComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            var config = rope.get(RopeRenderComponent.class);
            List<Vector> ropePoints = new ArrayList<>();
            addRopePoints(engine, rope, ropePoints);
            engine.graphics().world().drawPolygon(ropePoints, PolygonDrawOptions.outline(config.color).smoothing(PolygonDrawOptions.Smoothing.SPLINE).strokeWidth(config.strokeWidth));
        }
    }

    private static void addRopePoints(Engine engine, Entity rope, List<Vector> ropePoints) {
        JointComponent jointComponent = rope.get(JointComponent.class);
        ropePoints.add(rope.position());
        if (!jointComponent.joints.isEmpty()) {
            final var target = engine.environment().fetchById(jointComponent.joints.getFirst().targetEntityId);
            addRopePoints(engine, target, ropePoints);
        }
    }
}
