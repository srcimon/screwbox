package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class DebugJointsSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.environment().fetchAllHaving(PhysicsComponent.class).forEach(o -> engine.graphics().world().drawCircle(o.position(), o.bounds().width() / 2.0, OvalDrawOptions.filled(Color.BLUE)));
        engine.environment().fetchAllHaving(JointComponent.class).forEach(o -> {
            for (var joint : o.get(JointComponent.class).joints) {
                var targetId = joint.targetEntityId;
                engine.environment().tryFetchById(targetId).ifPresent(target -> engine.graphics().world().drawLine(o.position(), target.position(), LineDrawOptions.color(Color.BLUE).strokeWidth(2)));
            }
        });
    }
}
