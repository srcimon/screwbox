package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

@Deprecated
@ExecutionOrder(Order.SIMULATION_EARLY)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final double delta = engine.loop().delta();
            for (var target : engine.environment().fetchAllHaving(CameraTargetComponent.class)) {
                var configuration = target.get(CameraTargetComponent.class);
                double targetX = player.get(PhysicsComponent.class).velocity.x() * 1;
                double targetY = player.get(PhysicsComponent.class).velocity.y() * 1;
                double actualX = stepTowards(configuration.offset.x(), targetX, 100 * delta);
                double actualY = stepTowards(configuration.offset.y(), targetY, 100 * delta);
                //TODO keep within window
                var viewport = engine.graphics().viewport(configuration.viewportId);
                Canvas canvas = viewport.get().canvas();
                configuration.offset = Vector.$( //TODO clamp not working well vertically
                    Math.clamp(actualX, -canvas.width() / 2.0, canvas.width() / 2.0),
                    Math.clamp(actualY, -canvas.height() / 2.0, canvas.height() / 2.0));
                //TODO finish up
//                engine.graphics().world().drawOval(Vector.$(targetX, targetY).add(target.position()), 4, 4, OvalDrawOptions.filled(Color.BLUE.opacity(0.75)).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
//                engine.graphics().world().drawOval(Vector.$(actualX, actualY).add(target.position()), 4, 4, OvalDrawOptions.outline(Color.RED).strokeWidth(4).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            }
        });
    }

    //TODO math util
    //TODO reuse where possible
    public static double stepTowards(final double value, final double targetValue, final double step) {
        if (Math.abs(targetValue - value) <= step) {
            return targetValue;
        }
        return value + Math.signum(targetValue - value) * step;
    }

    public static void main(String[] args) {
        System.out.println(stepTowards(10, 50, 0.25));
    }
}