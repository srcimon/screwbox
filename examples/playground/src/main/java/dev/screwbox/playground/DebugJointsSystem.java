package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class DebugJointsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.environment().fetchAllHaving(PhysicsComponent.class).forEach(o -> {
            engine.graphics().world().drawCircle(o.position(), o.bounds().width() / 2.0, CircleDrawOptions.outline(Color.BLUE).strokeWidth(2));
        });
        engine.environment().fetchAllHaving(JointComponent.class).forEach(o -> {
            var targetId = o.get(JointComponent.class).targetEntityId;
            var target = engine.environment().fetchById(targetId);
            engine.graphics().world().drawCircle(o.position(), o.bounds().width() / 2.0, CircleDrawOptions.filled(Color.BLUE));
            engine.graphics().world().drawLine(o.position(), target.position(), LineDrawOptions.color(Color.BLUE).strokeWidth(2));
        });
    }
}
