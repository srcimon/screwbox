package dev.screwbox.platformer.systems;

import dev.screwbox.core.Bounds;
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
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

@Deprecated
@ExecutionOrder(Order.SIMULATION_EARLY)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);
    //TODO finish up
    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final double delta = engine.loop().delta();
            for (var target : engine.environment().fetchAllHaving(CameraTargetComponent.class)) {
                var configuration = target.get(CameraTargetComponent.class);
                Vector velocity = player.get(PhysicsComponent.class).velocity;
                var targetV = Vector.of(velocity.x() * 1.0, velocity.y() * 0.25);
                var actzualV = configuration.offset.lerp(targetV, 5 * delta);
                var viewport = engine.graphics().viewport(configuration.viewportId);

                configuration.offset = viewport
                    .map(Viewport::canvas)
                    .map(canvas -> Bounds.atOrigin(-canvas.width() / 4.0, -canvas.height() / 4.0, canvas.width() / 2.0, canvas.height() / 2.0).clamp(actzualV))
                    .orElse(actzualV);
            }
        });
    }

}