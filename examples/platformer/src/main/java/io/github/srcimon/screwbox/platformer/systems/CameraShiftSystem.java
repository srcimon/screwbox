package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final double delta = engine.loop().delta(100);
            for (var target : engine.environment().fetchAllHaving(CameraTargetComponent.class)) {
                var configuration = target.get(CameraTargetComponent.class);
                configuration.shift = player.get(RenderComponent.class).options.isFlipHorizontal()
                        ? Vector.x(Math.max(-50, configuration.shift.x() - configuration.followSpeed * delta))
                        : Vector.x(Math.min(50, configuration.shift.x() + configuration.followSpeed * delta));
            }
        });
    }
}
