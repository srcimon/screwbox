package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import java.util.ArrayList;
import java.util.List;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class DebugJointsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
//        engine.environment().fetchAllHaving(PhysicsComponent.class).forEach(o -> engine.graphics().world().drawCircle(o.position(), o.bounds().width() / 8.0, OvalDrawOptions.filled(Color.BLUE)));

        List<Vector> positions = new ArrayList<>();
        engine.environment().fetchAllHaving(JointDrawComponent.class).forEach(o -> {
            addJointsFromEntity(engine, o, positions);
        });

        engine.graphics().world().drawPolygon(positions, PolygonDrawOptions.outline(Color.BLUE).strokeWidth(2).smoothing(PolygonDrawOptions.Smoothing.SPLINE));
    }

    private static void addJointsFromEntity(Engine engine, Entity entity, List<Vector> positions) {
        for (var joint : entity.get(JointComponent.class).joints) {
            var targetId = joint.targetEntityId;
            positions.add(entity.position());
            engine.environment().tryFetchById(targetId).ifPresent(target -> {
                addJointsFromEntity(engine, target, positions);
            });
        }
    }
}
