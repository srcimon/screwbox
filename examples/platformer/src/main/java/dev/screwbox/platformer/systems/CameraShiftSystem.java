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
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

@ExecutionOrder(Order.SIMULATION_EARLY)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final double delta = engine.loop().delta(100);
            for (var target : engine.environment().fetchAllHaving(CameraTargetComponent.class)) {
                var configuration = target.get(CameraTargetComponent.class);
                double targetX = player.get(PhysicsComponent.class).velocity.x()*1;
                double targetY = player.get(PhysicsComponent.class).velocity.y()*0.5;
                double actualX = approachTargetSmoothing(configuration.offset.x(), targetX, 0.05 * delta);
                double actualY = approachTargetSmoothing(configuration.offset.y(), targetY, 0.05 * delta);
                //TODO keep within window
                configuration.offset = Vector.$(actualX, actualY);
                //TODO finish up
                engine.graphics().world().drawOval(Vector.$(targetX, targetY).add(target.position()), 4, 4, OvalDrawOptions.filled(Color.BLUE.opacity(0.75)).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                engine.graphics().world().drawOval(Vector.$(actualX, actualY).add(target.position()), 4, 4, OvalDrawOptions.outline(Color.RED).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            }
        });
    }

    //TODO math util
    //TODO reuse where possible
    public static double approachTargetSmoothing(double current, double target, double speed) {
        // Verhindert Ruckeln bei unregelmäßigen Frameraten
        double interpolationFactor = 1.0 - Math.exp(-speed);
        return current + (target - current) * interpolationFactor;
    }
}