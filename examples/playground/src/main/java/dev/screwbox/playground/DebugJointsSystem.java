package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.playground.joint.Joint;
import dev.screwbox.playground.joint.FlexBodyComponent;

import static dev.screwbox.core.environment.Order.SIMULATION;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class DebugJointsSystem implements EntitySystem {

    private static final LineDrawOptions LINE_OPTIONS = LineDrawOptions.color(Color.BLUE).strokeWidth(2).drawOrder(SIMULATION.drawOrder());

    @Override
    public void update(Engine engine) {
        engine.environment().fetchAllHaving(PhysicsComponent.class).forEach(o -> engine.graphics().world().drawCircle(o.position(), o.bounds().width() / 2.0, OvalDrawOptions.filled(Color.RED)));
        engine.environment().fetchAllHaving(FlexBodyComponent.class).forEach(o -> {
            FlexBodyComponent linkJointComponent = o.get(FlexBodyComponent.class);
            drawJoint(engine, o, linkJointComponent.joint);
//            for (var joint : jointComponent.additionalJoints) {
//                drawJoint(engine, o, joint);
//            }
        });
    }

    private static void drawJoint(Engine engine, Entity o, Joint joint) {
        var targetId = joint.targetEntityId;
        engine.environment().tryFetchById(targetId).ifPresent(target -> engine.graphics().world().drawLine(o.position(), target.position(), LINE_OPTIONS));
    }
}
