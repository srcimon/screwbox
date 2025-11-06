package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

public class RopeRenderSystem implements EntitySystem {

    private static final Archetype ROPES = Archetype.ofSpacial(RopeRenderComponent.class, JointComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var rope : engine.environment().fetchAll(ROPES)) {
            List<Vector> ropePoints = new ArrayList<>();
            addRopePoints(engine, rope, ropePoints);
            engine.graphics().world().drawPolygon(ropePoints, PolygonDrawOptions.outline(Color.ORANGE).smoothing(PolygonDrawOptions.Smoothing.SPLINE).strokeWidth(3));
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
